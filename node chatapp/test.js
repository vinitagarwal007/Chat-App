const db = require("./dbconnection");
db.connect();
db.doquery("select * from userregister", (res) => {
  console.log(res);
});
