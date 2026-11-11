db = db.getSiblingDB("pas_db");

db.createCollection("users");

db.users.createIndex(
    {login: 1},
    {unique: true, collation: {locale: "en-US", strength: 2}}
);

db.createCollection("resources");

db.createCollection("allocations");