const express = require("express");
const notification = require("./notification");
const db = require("./dbconnection");
const { connect } = require("http2");
db.connect("chatap");
const app = express();
const server = require("http").Server(app);
const io = require("socket.io").listen(server);
const test = "nvls";
const connected = {};
const disconnected = {};
app.set("view engine", "ejs");
app.use(express.static("public"));
app.get("/", (req, res) => {
  res.send("Invalid Url");
});
app.get("/:conte", (req, res) => {
  var content = req.params.conte;
  res.render("room", { content: req.params.conte });
});
io.on("connection", (socket) => {
  socket.emit("status", "Conneted");
  console.log("Incomming Connection");
  console.log(socket.handshake.query);
  socket.emit("connected");

  var sqlquery =
    "insert into userregister values('" +
    socket.handshake.query.number +
    "','" +
    socket.handshake.query.user +
    "','" +
    socket.handshake.query.id +
    "')";
  db.doquery(sqlquery, (res) => {});
  connected[socket.id] = socket.handshake.query.number;
  console.log("connected");
  console.log(connected);
  socket.broadcast.emit("newuser", socket.handshake.query.user);

  for (id in disconnected) {
    if (disconnected[id] == socket.handshake.query.number) {
      delete disconnected[id];
    }
  }
  console.log("disconnect");
  console.log(disconnected);

  socket.on("msg", (msg) => {
    console.log(msg);
    socket.broadcast.emit("msg", msg);
    var msgjson = JSON.parse(msg);
    var c = 0;
    for (socid in disconnected) {
      db.doquery(
        "select * from userregister where number = '" +
          disconnected[socid] +
          "'",
        (res) => {
          console.log(res);
          console.log(res[0].playerid);
          const message = {
            app_id: "62e7212c-d7df-4c9c-8451-246b84c3e3d3",
            contents: { en: msgjson.msg },
            include_player_ids: [res[0].playerid],
            headings: { en: msgjson.name },
            priority: 10,
            android_group: 1,
          };
          notification.sendNotification(message);
        }
      );
    }

    // if (disconnected != null) {
    //   const message = {
    //     app_id: "62e7212c-d7df-4c9c-8451-246b84c3e3d3",
    //     contents: { en: msgjson.name + "\n" + msgjson.msg },
    //     included_segments: ["All"],
    //     headings: { en: "Global Chat" },
    //     priority: 10,
    //   };
    //   notification.sendNotification(message);
    // }
  });
  var connectionmsg = {};
  socket.on("connectserver", (msg) => {
    console.log("requested login by :" + msg);
  });
  socket.on("disconnect", () => {
    console.log("user Disconnected");
    for (var socket_id in connected) {
      if (socket_id == socket.id) {
        io.emit("userleft", connected[socket.id]);
        disconnected[socket_id] = connected[socket.id];
        delete connected[socket_id];
        console.log(disconnected);
        console.log("connected");
        console.log(connected);
      }
    }
  });
});
server.listen(80);
