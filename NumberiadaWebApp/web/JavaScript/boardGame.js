window.myPlayerIndex = 0;

$(function () {
 $("#BackButton").click(function () {
  if (window.quited === false)
  {
   quit();
  }
  window.location.href = "mainMenu.html";

 });
 $("#Error").hide();

 initilazeGame();
});

function initilazeGame() {
 $.ajax({
  type: 'POST',
  dataType: 'json',
  url: "Get-Game-Players",
  timeout: 6000,
  success: function (playersData, textStatus, jqXHR) {
   //window.myPlayerIndex = playersData.myPlayerIndex;
   updateCurrentPlayer(playersData.checkPlayer);
   setPlayersBar(0, playersData.checkPlayer);
   setPlayersBar(0, playersData.checkPlayer2);
   //buildPlayersBar(playersData);
   //buildGameBoardStructure();
   //setCellClick();
   //showPlayerTurn(playersData.currPlayer);
  },
  error: function (jqXHR, textStatus, errorThrown) {
   if (textStatus === "timeout") {
    $("#Error").text("Server Timeout setAction. Try again..").show();
   }
   else {
    $("#Error").text("Something went wrong setAction. Try again..").show();
   }
  }
 });
}

function updateCurrentPlayer(currPlayer) {
 $("#currentPlayerName").text(currPlayer.name.value);
 $("#currentPlayerScore").text(currPlayer.score.value);
 //$("#currentPlayerColor").text(currPlayer.color.value);
 $("#currentPlayerType").text(currPlayer.playerType.value.toString());

 }



function buildGameBoardStructure() {
 var numOfPieceInRow = [1, 2, 3, 4, 13, 12, 11, 10, 9, 10, 11, 12, 13, 4, 3, 2, 1];
 board = [];
 var count = 0;
 for (i = 0; i < numOfPieceInRow.length; i++) {
  var cells = $('<tr>', {class: 'line', align: 'center'});
  for (j = 0; j < numOfPieceInRow[i]; j++) {
   cells.append($('<button>', {class: 'Cell', id: count, "row": (i + 1), "col": (j + 1), align: 'center'}));
   count++;
  }
  $("table#GameBoardTable > tbody").append(cells);
 }
}

function setCellClick() {
 $(".cell").click(function ()
 {
  var row = $(this).attr("row");
  var col = $(this).attr("col");
  $.ajax({
   type: 'POST',
   data: {row: row, col: col, actionType: "userIteration"},
   url: "User-Iteration",
   timeout: 6000,
   success: function () {
   },
   error: function (textStatus) {
    setCellClick();
    $("#Error").text("A temporary problem").show();
   }
  });
  return false;
 });
}

function buildPlayersBar(players) {
 for (i = 0; i < players.numOfPlayers; i++) {
  var player = players.players[i];
 // var playerPic;
  //playerPic = player.playerType === "COMPUTER" ? "ComputerAvatar.png" : "HumanAvatar.png";
  setPlayersBar(i, player);
 }
}

function setPlayersBar(index, player) {
 $("#playersNamesBody").append("<br>  <tr class=PlayerName>" +
     "<td style=\"text-align: left;\">" + player.name.value + "</td>" +
     "<td style=\"text-align: left;\">" + player.playerType.value + "</td>" +
     "<td style=\"text-align: left;\">" + player.score.value + "</td>" +
     "</tr>");

}
function showPlayerTurn(currPlayer) {
 $(".PlayerBox").popover("destroy").removeClass("MyTurn");
 if (currPlayer === window.myPlayerIndex) {
  showTurn("Your Turn", currPlayer);
 } else {
  showTurn("Playing", currPlayer);
 }
}

function showTurn(turnType, currPlayer)
{
 $(".ActivePlayer").eq(currPlayer).addClass("MyTurn").popover({
  trigger: "manual",
  placement: "right",
  content: turnType
 }).popover("show");
}