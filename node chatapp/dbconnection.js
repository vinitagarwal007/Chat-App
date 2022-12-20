const mysql = require("mysql2");
const pool = mysql.createPool({
  host: "localhost",
  user: "root",
  database: "chatap",
  password: "vinitagarwal",
});
function connect(dbname) {
  pool.getConnection((err, con) => {
    if (con) {
      console.log("db Connected");
    } else {
      console.log("db error:" + err);
    }
  });
}

function doquery(sqlquery, callback) {
  pool.query(sqlquery, (err, res) => {
    if (err) {
      callback(err);
    } else {
      callback(res);
    }
  });
}
function disconnect() {
  console.log("vinit");
  connection.destroy;
}
exports.doquery = doquery;
exports.connect = connect;
exports.disconnect = disconnect;
