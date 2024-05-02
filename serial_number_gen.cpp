#include <iostream>
#include <fstream>
#include <uuid/uuid.h>
#include <cstring>


using namespace std;

int main() { 
    ofstream file("query.txt");
    
    uuid_t uuid;
    char uuid_str[16];
    string query = "insert into Released_kit values(";

    for (int i = 1; i < 101; i++)
    {
        uuid_generate(uuid);
        uuid_unparse(uuid, uuid_str);
        file << query + char(i) + ", " + uuid_str + ");\n";
    }
    file.close();
}