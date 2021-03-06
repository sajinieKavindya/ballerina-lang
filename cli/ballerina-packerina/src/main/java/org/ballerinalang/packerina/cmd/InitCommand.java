/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.packerina.cmd;

import org.ballerinalang.launcher.BLauncherCmd;
import org.ballerinalang.packerina.init.InitHandler;
import org.ballerinalang.packerina.init.models.FileType;
import org.ballerinalang.packerina.init.models.ModuleMdFile;
import org.ballerinalang.packerina.init.models.SrcFile;
import org.ballerinalang.toml.model.Manifest;
import org.wso2.ballerinalang.compiler.util.ProjectDirConstants;
import org.wso2.ballerinalang.util.RepoUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.ballerinalang.packerina.cmd.Constants.INIT_COMMAND;

/**
 * Init command for creating a ballerina project.
 */
@CommandLine.Command(name = INIT_COMMAND, description = "initialize a Ballerina project")
public class InitCommand implements BLauncherCmd {

    public static final String DEFAULT_VERSION = "0.0.1";
    private static final String USER_DIR = "user.dir";
    private static final PrintStream errStream = System.err;
    private final Path homePath = RepoUtils.createAndGetHomeReposPath();
    private boolean alreadyInitializedProject = false;
    private boolean manifestExistInProject = false;
    private PrintStream out = System.out;

    @CommandLine.Option(names = {"--interactive", "-i"})
    private boolean interactiveFlag;

    @CommandLine.Option(names = {"--help", "-h"}, hidden = true)
    private boolean helpFlag;

    private static boolean isDirEmpty(final Path directory) throws IOException {

        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            //Check whether the OS is MacOS and the folder contains .DS_Store file
            Iterator pathIterator = dirStream.iterator();
            if (!pathIterator.hasNext()) {
                return true;
            }
            Path path = (Path) pathIterator.next();
            Path fileName = path.getFileName();
            return fileName != null && fileName.toString().equals(ProjectDirConstants.DS_STORE_FILE) &&
                    !pathIterator.hasNext();
        }
    }

    @Override
    public void execute() {
        // Get source root path.
        Path projectPath = Paths.get(System.getProperty(USER_DIR));
        try {
            // Check if it is a project
            boolean isProject = Files.exists(projectPath.resolve(ProjectDirConstants.DOT_BALLERINA_DIR_NAME));
            if (isProject) {
                alreadyInitializedProject = true;
                manifestExistInProject = Files.exists(projectPath.resolve(ProjectDirConstants.MANIFEST_FILE_NAME));
            }
            // If the current directory is not a project traverse and check down and up
            if (!alreadyInitializedProject) {
                // Recursively traverse down
                Optional<Path> childDotBallerina = Files.walk(projectPath)
                        .filter(path -> Files.isDirectory(path) &&
                                path.toFile().getName().equals(ProjectDirConstants.DOT_BALLERINA_DIR_NAME))
                        .findFirst();
                if (childDotBallerina.isPresent()) {
                    errStream.println("A ballerina project is already initialized in " +
                            childDotBallerina.get().toFile().getParent());
                    return;
                }
                // Recursively traverse up till the root
                Path projectRoot = findProjectRoot(projectPath);
                if (projectRoot != null) {
                    errStream.println("Directory is already within a ballerina project :" + projectRoot.toString());
                    return;
                }
            }
        } catch (IOException ignore) {
        }

        Scanner scanner = new Scanner(System.in, Charset.defaultCharset().name());
        try {
            Manifest manifest = null;

            if (helpFlag) {
                String commandUsageInfo = BLauncherCmd.getCommandUsageInfo(INIT_COMMAND);
                errStream.println(commandUsageInfo);
                return;
            }

            List<SrcFile> sourceFiles = new ArrayList<>();
            List<ModuleMdFile> moduleMdFiles = new ArrayList<>();
            boolean validInput = false;
            boolean firstPrompt = true;
            if (interactiveFlag) {

                if (!manifestExistInProject) {
                    // Check if Ballerina.toml file needs to be created.
                    out.print("Create Ballerina.toml [yes/y, no/n]: (y) ");
                    String createToml = scanner.nextLine().trim();

                    manifest = createManifest(scanner, createToml);
                }
                // If its already an initialized project
                if (alreadyInitializedProject) {
                    out.print("Create modules [yes/y, no/n]: (y) ");
                    firstPrompt = false;
                    String input = scanner.nextLine().trim();
                    if (input.equalsIgnoreCase("n")) {
                        out.println("Ballerina project not reinitialized");
                        return;
                    }
                }

                String srcInput;
                do {
                    // Following will be the first prompt and it will create a service by default. This is to align
                    // with the non-interactive implementation.
                    if (firstPrompt) {
                        // Here if the user presses enter or "s" a service will be created (This will have the same
                        // behavior as running ballerina init without the interactive mode)
                        out.print("Ballerina source [service/s, main/m, finish/f]: (s) ");
                    } else {
                        // Following will be prompted after the first prompt
                        // Here if the user presses enter, "f" or "finish" the command will be exited. If user gives
                        // "m" a main function and "s" a service will be created.
                        out.print("Ballerina source [service/s, main/m, finish/f]: (f) ");
                    }
                    srcInput = scanner.nextLine().trim();

                    if (srcInput.equalsIgnoreCase("service") || srcInput.equalsIgnoreCase("s")
                            || (srcInput.isEmpty() && firstPrompt)) {
                        String packageName;
                        do {
                            out.print("Module for the service: (no module) ");
                            packageName = scanner.nextLine().trim();
                        } while (!validatePkgName(projectPath, packageName));
                        SrcFile srcFile = new SrcFile(packageName, FileType.SERVICE);
                        sourceFiles.add(srcFile);
                        SrcFile srcTestFile = new SrcFile(packageName, FileType.SERVICE_TEST);
                        sourceFiles.add(srcTestFile);
                        if (!packageName.isEmpty()) {
                            ModuleMdFile moduleMdFile = new ModuleMdFile(packageName, FileType.SERVICE);
                            moduleMdFiles.add(moduleMdFile);
                        }
                        firstPrompt = false;
                    } else if (srcInput.equalsIgnoreCase("main") || srcInput.equalsIgnoreCase("m")) {
                        String packageName;
                        do {
                            out.print("Module for the main: (no module) ");
                            packageName = scanner.nextLine().trim();
                        } while (!validatePkgName(projectPath, packageName));
                        SrcFile srcFile = new SrcFile(packageName, FileType.MAIN);
                        sourceFiles.add(srcFile);
                        SrcFile srcTestFile = new SrcFile(packageName, FileType.MAIN_TEST);
                        sourceFiles.add(srcTestFile);
                        if (!packageName.isEmpty()) {
                            ModuleMdFile moduleMdFile = new ModuleMdFile(packageName, FileType.MAIN);
                            moduleMdFiles.add(moduleMdFile);
                        }
                        firstPrompt = false;
                    } else if (srcInput.isEmpty() || srcInput.equalsIgnoreCase("f") ||
                            srcInput.equalsIgnoreCase("finish")) {
                        validInput = true;
                        firstPrompt = false;
                    } else {
                        out.println("Invalid input");
                    }
                } while (!validInput);

                out.print("\n");
            } else {
                manifest = new Manifest();
                manifest.setName(guessOrgName());
                manifest.setVersion(DEFAULT_VERSION);
                if (isDirEmpty(projectPath)) {
                    SrcFile srcFile = new SrcFile("", FileType.SERVICE);
                    sourceFiles.add(srcFile);
                }
            }

            InitHandler.initialize(projectPath, manifest, sourceFiles, moduleMdFiles);
            if (!alreadyInitializedProject) {
                out.println("Ballerina project initialized");
            } else {
                out.println("Ballerina project reinitialized");
            }

        } catch (IOException e) {
            out.println("Error occurred while creating project: " + e.getMessage());
        }
    }

    /**
     * Create a manifest object.
     *
     * @param scanner    scanner object
     * @param createToml create toml or not
     * @return manifest object
     */
    private Manifest createManifest(Scanner scanner, String createToml) {

        Manifest manifest = new Manifest();
        if (createToml.equalsIgnoreCase("yes") || createToml.equalsIgnoreCase("y")
                || createToml.isEmpty()) {

            String defaultOrg = guessOrgName();

            String orgName;
            do {
                out.print("Organization name: (" + defaultOrg + ") ");
                orgName = scanner.nextLine().trim();
            } while (!validateOrgName(orgName));
            // Set org-name
            manifest.setName(orgName.isEmpty() ? defaultOrg : orgName);
            String version;
            do {
                out.print("Version: (" + DEFAULT_VERSION + ") ");
                version = scanner.nextLine().trim();
                version = version.isEmpty() ? DEFAULT_VERSION : version;
            } while (!validateVersion(out, version));

            manifest.setVersion(version);
        }
        return manifest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return INIT_COMMAND;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printLongDesc(StringBuilder out) {
        out.append("Initializes a Ballerina Project. \n");
        out.append("\n");
        out.append("Use --interactive or -i to create a ballerina project in interactive mode.\n");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printUsage(StringBuilder out) {
        out.append("  ballerina init [-i] \n");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParentCmdParser(CommandLine parentCmdParser) {
    }

    /**
     * Validates the version is a semver version.
     *
     * @param versionAsString The version.
     * @return True if valid version, else false.
     */
    private boolean validateVersion(PrintStream out, String versionAsString) {
        String semverRegex = "((?:0|[1-9]\\d*)\\.(?:0|[1-9]\\d*)\\.(?:0|[1-9]\\d*))";
        boolean matches = Pattern.matches(semverRegex, versionAsString);
        if (!matches) {
            out.println("--Invalid version: \"" + versionAsString + "\"");
        }
        return matches;
    }

    private String guessOrgName() {
        String guessOrgName = System.getProperty("user.name");
        if (guessOrgName == null) {
            guessOrgName = "my_org";
        } else {
            guessOrgName = guessOrgName.toLowerCase(Locale.getDefault());
        }
        return guessOrgName;
    }

    /**
     * Validates the org-name.
     *
     * @param orgName The org-name.
     * @return True if valid org-name, else false.
     */
    private boolean validateOrgName(String orgName) {
        if (RepoUtils.isReservedOrgName(orgName)) {
            out.println("--Invalid organization name: \'" + orgName + "\'. 'ballerina' and 'ballerinax' are reserved " +
                    "organization names that are used by Ballerina");
            return false;
        }
        boolean matches = RepoUtils.validateOrg(orgName);
        if (!matches) {
            out.println("--Invalid organization name: \'" + orgName + "\'. Organization name can only contain " +
                    "lowercase alphanumerics and underscores and the maximum length is 256 characters");
        }
        return matches;
    }

    /**
     * Validates the module name.
     *
     * @param projectPath
     * @param pkgName     The module name.
     * @return True if valid module name, else false.
     */
    private boolean validatePkgName(Path projectPath, String pkgName) {
        if (validateExistingModules(projectPath, pkgName)) {
            return false;
        }
        if (pkgName.isEmpty()) {
            return true;
        }
        boolean matches = RepoUtils.validatePkg(pkgName);
        if (!matches) {
            out.println("--Invalid module name: \'" + pkgName + "\'. Module name can only contain " +
                    "alphanumerics, underscores and periods and the maximum length is 256 characters");
        }
        return matches;
    }

    /**
     * Find the project root by recursively up to the root.
     *
     * @param projectDir project path
     * @return project root
     */
    private Path findProjectRoot(Path projectDir) {
        Path path = projectDir.resolve(ProjectDirConstants.DOT_BALLERINA_DIR_NAME);
        if (!path.equals(homePath) && java.nio.file.Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            return projectDir;
        }
        Path parentsParent = projectDir.getParent();
        if (null != parentsParent) {
            return findProjectRoot(parentsParent);
        }
        return null;
    }

    /**
     * Validate existing modules.
     *
     * @param projectPath project path
     * @param moduleNames modules name
     * @return if the module name already exists
     */
    private boolean validateExistingModules(Path projectPath, String moduleNames) {
        if (alreadyInitializedProject) {
            List<Path> modules = new ArrayList<>();
            try {
                modules = Files.list(projectPath).map(Path::getFileName).collect(Collectors.toList());
            } catch (IOException ignore) {
            }
            if (modules.contains(Paths.get(moduleNames))) {
                out.println("Module already exists");
                return true;
            }
        }
        return false;
    }
}
