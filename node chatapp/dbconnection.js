const mysql = require("mysql");
var connectionstring = {
  host: "192.168.18.8",
  user: "vinit",
  password: "vinitagarwal",
};
var connection = null;
function connect(dbname) {
  connectionstring.database = dbname;
  connection = mysql.createConnection(connectionstring);
  connection.connect(function (err) {
    if (err) {
      console.error("error connecting: " + err.stack);
    }
    console.log("connected to mysql as id " + connection.threadId);
  });
}

function doquery(sqlquery, callback) {
  connection.query(sqlquery, (err, res) => {
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
