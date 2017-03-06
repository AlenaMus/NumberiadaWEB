window.myPlayerIndex = 0;

$(function() {

 initilazeGame();

});


//$(function () {
// $("#BackButton").click(function () {
 //  if (window.quited === false)
 //  {
 //   quit();
 //  }
 //  window.location.href = "mainMenu.html";
 //
 // });
 // $("#Error").hide();
 //
 // initilazeGame();
//});

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
   buildGameBoardStructure();
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
 $.ajax({
  type: 'GET',
  dataType: 'json',
  url: "getGameBoard",
  timeout: 6000,
  success: function (board, textStatus, jqXHR) {
   if(board!=='Error'){
       buildGameBoard(board.gameBoard,board.boardSize);
   }else{

   }

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


function buildGameBoard(board,size) {

 var value;
 var color;
 var classColor;
 var boardRow;
 var buttId;
 var cell;

 var gameBoard = $('#GameBoardTable');

 for (var i = 0; i < size; i++) {
   boardRow = $('<tr>', {class: 'boardRow', align: 'center'});
  //boardRow.css()
  boardRow.appendTo(gameBoard);

  for (var j = 0; j < size; j++) {

   value = board[i][j].squareValue.value;
   color = board[i][j].color.value;
   classColor = chooseSquare(color);
   //classColor = chooseClass(color);
   buttId = 'butt'+ i+j;

    cell = $('<td>', {class: 'cell', align: 'center'});
    var div = $('<div>',{class:'divButt',align: 'center'});
    var button = $('<button/>', {class: 'square',text: value, id: 'butt', "row": i, "col": j,align: 'center',click: clickedSquare()});
        button.id = buttId;
        button.css("background-color",classColor);
    div.append(button);
    cell.append(div);
    cell.appendTo(boardRow);


  }
 }

}


function chooseSquare(color){
 var setcolor;
 switch (color){
  case 0: setcolor = '#b8a3a3';
   break;
  case 1:setcolor = '#f44336';
   break;
  case 2:setcolor = '#008CBA';
   break;
  case 3:setcolor = '#4CAF50';
   break;
  case 4:setcolor = '#f4d42b';
   break;
  case 5:setcolor = '#9d3b9c';
   break;
  case 6:setcolor = '#f8609f';
   break;
  case 100:setcolor ='#89F455';
   break;
 }
 return setcolor;
}

function chooseClass(color){
 var setcolor;
 switch (color){
  case 0: setcolor = 'gray';
   break;
  case 1:setcolor = 'red';
   break;
  case 2:setcolor = 'blue';
   break;
  case 3:setcolor = 'green';
   break;
  case 4:setcolor = 'yellow';
   break;
  case 5:setcolor = 'violet';
   break;
  case 6:setcolor = 'orange';
   break;
  case 100:setcolor ='marker';
   break;
 }
 return setcolor;
}


function clickedSquare(){
}


  function setCellClick() {
   $(".cell").click(function () {
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

  function showTurn(turnType, currPlayer) {
   $(".ActivePlayer").eq(currPlayer).addClass("MyTurn").popover({
    trigger: "manual",
    placement: "right",
    content: turnType
   }).popover("show");
  }

