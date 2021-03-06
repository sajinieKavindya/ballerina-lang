
import c.d;

function testJsonToRecord() returns d:Person|error {
    json j = {"name":"John", "age":30, "adrs": {"street": "20 Palm Grove", "city":"Colombo 03", "country":"Sri Lanka"}};
    var p = check d:Person.convert(j);
    return p;
}

function testMapToRecord() returns d:Person|error {
    map<any> m1 = {"street": "20 Palm Grove", "city":"Colombo 03", "country":"Sri Lanka"};
    map<any> m2 = {"name":"John", "age":30, "adrs": m1};
    d:Person p = check d:Person.convert(m2);
    return p;
}
