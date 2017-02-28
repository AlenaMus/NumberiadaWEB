window.myPlayerIndex = 0;

 $(function () {



});




//
// function buildGameBoardStructure() {
//     var numOfPieceInRow = [1, 2, 3, 4, 13, 12, 11, 10, 9, 10, 11, 12, 13, 4, 3, 2, 1];
//     board = [];
//     var count = 0;
//     for (i = 0; i < numOfPieceInRow.length; i++) {
//         var cells = $('<tr>', {class: 'line', align: 'center'});
//         for (j = 0; j < numOfPieceInRow[i]; j++) {
//             cells.append($('<button>', {class: 'Cell', id: count, "row": (i + 1), "col": (j + 1), align: 'center'}));
//             count++;
//         }
//         $("table#GameBoardTable > tbody").append(cells);
//     }
// }
//
// function setCellClick() {
//     $(".cell").click(function ()
//     {
//         var row = $(this).attr("row");
//         var col = $(this).attr("col");
//         $.ajax({
//             type: 'POST',
//             data: {row: row, col: col, actionType: "userIteration"},
//             url: "UserIteration",
//             timeout: 6000,
//             success: function () {
//             },
//             error: function (textStatus) {
//                 setCellClick();
//                 $("#Error").text("A temporary problem").show();
//             }
//         });
//         return false;
//     });
// }
//
// function buildPlayersBar(players) {
//     for (i = 0; i < players.numOfPlayers; i++) {
//         var player = players.players[i];
//         var playerPic;
//         playerPic = player.playerType === "COMPUTER" ? "ComputerAvatar.png" : "HumanAvatar.png";
//         setPlayersBar(i, player.NAME, playerPic);
//     }
// }
//
// function setPlayersBar(index, name, playerPic) {
//     var playerBar;
//     index < 3 ? playerBar = "div#PlayersBar" : playerBar = "div#PlayersBar2";
//     $(playerBar).append("<div index=" + index + ">" +
//             "<div class=\"col col-md-10 PlayerBox ActivePlayer\">" +
//             "<div class=\"row\">" +
//             "<div class=\"col-md-10  PlayerName\"><span class='label label-primary'>" + name + "</span></div>" +
//             "</div>" +
//             "<div class=\"row\">" +
//             "<div class=\"\"><img class=\"PlayerImage\" src=\"resource/images/player" + (index + 1) + playerPic + "\"></div>" +
//             "</div>" +
//             "</div>" +
//             "</div>");
// }
//
// function showPlayerTurn(currPlayer) {
//     $(".PlayerBox").popover("destroy").removeClass("MyTurn");
//     if (currPlayer === window.myPlayerIndex) {
//         showTurn("Your Turn", currPlayer);
//     } else {
//         showTurn("Playing", currPlayer);
//     }
// }
//
// function showTurn(turnType, currPlayer)
// {
//     $(".ActivePlayer").eq(currPlayer).addClass("MyTurn").popover({
//         trigger: "manual",
//         placement: "right",
//         content: turnType
//     }).popover("show");
// }




