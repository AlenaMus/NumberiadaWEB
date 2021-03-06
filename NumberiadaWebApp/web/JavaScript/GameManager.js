window.playSound = true;
window.playerGameVersion = 0;
window.isComputer = false;
window.myPlayerIndex;
window.CurrPlayerIndex;
window.playersUpdates = 0;

window.intervalStartGame = 0;
window.intervalGameUpdates=0;

window.quited = false;
window.isGameStarted = false;


$(function() {

    initilazeGame();
    checkGameStart();

    $("#leaveGame").click(function () {
        quit();
    });

});

    $(window.intervalGameUpdates = setInterval(function () {
            getGameUpdate();
        }, 200)
    );

 $(window.intervalStartGame = setInterval(function ()
        {
            checkGameStart();
        }, 300)
    );

 $(window.playersUpdates = setInterval(function ()
        {
            updateGamePlayersList();
        }, 300)
    );


$("#quitButton").click(function () {
        quit();
});
$("#SoundButton").click(function () {
        window.playSound === true ? mute() : sound();
});
    $("#BackGroundAudio").trigger("play");




function quit() {

    var leaveGameDialog = $(document.createElement('div'));
    if(window.isComputer === true){
        leaveGameDialog.html("Computer Player Cannot Leave Game till it ends!");
        leaveGameDialog.dialog({
            modal: true,
            title: "ERROR",
            height: "auto",
            width: "auto"
        });
    }else {
        if (window.isGameStarted === true) {
            if (window.myPlayerIndex === window.CurrPlayerIndex) {
                window.quited = true;
                playerRetire();
            } else {
                leaveGameDialog.html("Please wait your turn to leave the game !");
                leaveGameDialog.dialog({
                    modal: true,
                    title: "Cannot leave game",
                    height: "auto",
                    width: "auto"
                });
            }
        } else {
            window.quited = true;
            playerRetire();
        }
    }
}


function mute() {
window.playSound = false;
$("audio").trigger("pause");
$("#SoundButton").attr({
src: "resource/images/sound_off.png",
title: "Mute",
alt: "Mute"});
}


function sound() {
window.playSound = true;
$("#BackGroundAudio").trigger("play");
$("#SoundButton").attr({
src: "resource/images/sound on.jpg",
title: "Sound",
alt: "Sound"});
}



function checkGameStart() {
    $.ajax({
        type: 'POST',
        url: "startGame",
        dataType: 'json',
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
           $('#hiUser').html('Hello '+ data.userName);
            if (data.succeedToStartGame === false) {
                $('#gameStatus').html(data.message);
            } else {
                window.isGameStarted = true;
                $('#gameStatus').html('Game Started!');
                clearInterval(window.intervalStartGame);
                if(data.message !== ""){
                    window.alert(data.message);
                }
                updateCurrentPlayer(data.currentPlayer);
                if (data.currentPlayer.playerType.value == 'Computer')
                {
                    setAction("computerIteration");
                }

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

function playerRetire()
{
    $.ajax({
        type: 'POST',
        data: {actionType: "quit", playerIndex: window.myPlayerIndex},
        url: "userIteration",
        timeout: 6000,
        success: function (message) {
                clearInterval(window.intervalGameUpdates);
                clearInterval(window.playersUpdates);
                window.location.href = 'LobbyPage.html';
        },
        error: function (textStatus) {
            if (textStatus === "timeout") {
                $("#Error").text("Server Timeout setAction. Try again..").show();
            }
            else {
                $("#Error").text("Something went wrong setAction. Try again..").show();
            }
        }
    });
    return false;
}

function setAction(actionType)
{
    $.ajax({
    type: 'POST',
    data: {actionType: actionType, playerIndex: window.myPlayerIndex},
    url: "userIteration",
    timeout: 6000,
    success: function (data) {
        updateBoardAfterMove(data);
        handelComputerTurn(data);
    },
    error: function (textStatus) {
    if (textStatus === "timeout") {
    $("#Error").text("Server Timeout setAction. Try again..").show();
    }
    else {
    $("#Error").text("Something went wrong setAction. Try again..").show();
    }
    }
});
return false;
}

function getGameUpdate()
{
    if(window.isGameStarted === true) {
        $.ajax({
            type: 'POST',
            data: {
                myPlayerGameVersion: window.playerGameVersion,
                playerIndex: window.myPlayerIndex,
                gameState: 'gameRunning'
            },
            url: "gameUpdates",
            dataType: "json",
            timeout: 6000,
            success: function (data, textStatus, jqXHR) {
                if ((data !== false) && (window.playerGameVersion !== data.latestGameVersion)) {
                    updateBoardAfterMove(data);

                    if (data.gameOver === true) {
                        clearInterval(window.intervalGameUpdates);
                        handelGameOver(data);

                    }
                    else {
                        handelComputerTurn(data);
                        updateCurrentPlayer(data.currentPlayer);
                        //showPlayerTurn(data.currentPlayerIndex);
                    }
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (textStatus === "timeout") {
                    $("#Error").text("Server Timeout getGameUpdate. Try again..").show();
                }
                else {
                    $("#Error").text("Something went wrong getGameUpdate. Try again..").show();
                }
            }
        });
        return false;
    }
}

function updateBoardAfterMove(data) {
    var time;
    time = data.computerTurn === true ? 500 : 200;

    if (data.cellToUpdate !== undefined && data.cellToUpdate !== null) {
        window.playerGameVersion++;
        window.CurrPlayerIndex = data.currentPlayer.playerIndex;

        data.cellToUpdate.forEach(function (square) {

            var value = square.squareValue.value;
            var point = square.location;
            var color = square.color.value;
            var buttId = '#butt' + point.row + point.col;
            var classColor = chooseSquare(color);
            $(buttId).html(value);
            $(buttId).css("background-color", classColor);

        });
    }
}

function updateCurrentPlayer(currPlayer) {
    var color = chooseColor(currPlayer.color.value);
    var fontColor = chooseSquare(currPlayer.color.value);
    window.CurrPlayerIndex = currPlayer.playerIndex;
    $("#currentPlayerName").text(currPlayer.name.value);
    $("#currentPlayerScore").text(currPlayer.score.value);
    $("#currentPlayerColor").text(color);
    $("#currentPlayerColor").css('color',fontColor);
    $("#currentPlayerType").text(currPlayer.playerType.value.toString());

}

function handelComputerTurn(data) {

    if((data.computerTurn === true )&& (data.allPlayersAreUpToDate === true)){
        setAction("computerIteration");
       }
    $("#endTurnButton").attr("disabled", data.computerTurn);
}


function handelGameOver(data) {

    var winnerPopup = $(document.createElement('div'));
    winnerPopup.html(data.winner);
    winnerPopup.dialog({
        modal: true,
        title: "Game Over",
        height: "auto",
        width: "auto",
        close: function (event, ui) {
            setGameOver();
        }
    });
}


function setGameOver() {
    $.ajax({
        type: 'POST',
        url: "gameUpdates",
        data: {myPlayerGameVersion: window.playerGameVersion, playerIndex: window.myPlayerIndex, gameState:'gameOver'},
        dataType: 'json',
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
              window.CurrPlayerIndex = 0;
              window.myPlayerIndex = 0;
            window.location.href = 'LobbyPage.html';
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (textStatus === "timeout") {
                $("#Error").text("Server Timeout setAction. Try again..").show();
            }
            else {
                $("#Error").text("Something went wrong in Game Over. Try again..").show();
            }
        }
    });


}

function updateGamePlayersList() {
    $.ajax({
        type: 'POST',
        url: "getGamePlayers",
        dataType: 'json',
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
            $("#playersInfoTable").empty();
              buildPlayersBar(data);
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




function initilazeGame() {
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: "getGamePlayers",
        timeout: 6000,
        success: function (playersData, textStatus, jqXHR) {

            window.myPlayerIndex = playersData.myPlayerIndex;
            window.isComputer = playersData.isComputer;
            buildPlayersBar(playersData);
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
        boardRow.appendTo(gameBoard);

        for (var j = 0; j < size; j++) {

            value = board[i][j].squareValue.value;
            color = board[i][j].color.value;
            classColor = chooseSquare(color);
            //classColor = chooseClass(color);
            buttId = 'butt'+ i+j;

            cell = $('<td>', {class: 'cell', align: 'center'});
            var div = $('<div>',{class:'divButt',align: 'center'});
            var button = $('<button/>', {class: 'square',text: value, id: buttId, align: 'center',row: i, col: j}); //id: 'butt',"row": i, "col": j,
            button.id = buttId;
            button.css("background-color",classColor);
            div.append(button);
            cell.append(div);
            cell.appendTo(boardRow);

            $('#'+buttId).on('click',function() {
                 var row = $(this).attr("row");
                 var col = $(this).attr("col");
                  clickedSquare(row,col);
             });


        }
    }

}


function clickedSquare(row,col){

    var error = $(document.createElement('div'));
    if(window.isGameStarted === true) {

        if (window.myPlayerIndex === window.CurrPlayerIndex) {

            $.ajax({
                type: 'POST',
                data: {row: row, col: col, actionType: "userIteration"},
                url: "userIteration",
                timeout: 6000,
                dataType: 'json',
                success: function (message) {
                    if(message === ""){}
                    else {
                        error.html(message);
                        error.dialog({
                            modal: true,
                            title: "Move Info",
                            height: "auto",
                            width: "auto"
                        });
                    }
                },
                error: function (textStatus) {
                    $("#Error").text("A temporary problem").show();
                }
            });
        }else{
            $("#Error").text("It is not your turn to move!");
            $("#Error").dialog({
                modal: true,
                title: "Game Board",
                height: "auto",
                width: "auto"
            });
        }
    }

}

function buildPlayersBar(players) {
    for (var i = 0; i < players.numOfPlayers; i++) {
        var player = players.players[i];
        setPlayersBar(i, player);
    }
}

function setPlayersBar(index, player) {

    var color = chooseColor(player.color.value);
    var fontColor = chooseSquare(player.color.value);

    $('#playersInfoTable').append("<tr class = playerInfo ><td class='playerCell'>" + index + "</td>" +
        "<td class='playerCell' >" + player.name.value + "</td>" + //style=\"text-align: left;\"
        "<td class='playerCell'>" + player.playerType.value + "</td>" +
        "<td class='playerCell' id ='playerColor'>" + color + "</td>" +
        "<td class='playerCell'>" + player.score.value + "</td></tr>"); //style=\"text-align: left;\"

    $('#playerColor').attr('id','color'+ player.color.value);
    $('#color'+player.color.value).css({'color':fontColor});

    $('.playerCell').css({
        "margin-right": "10px",
        "margin-left ":"10px",
        "margin-bottom":"5px",
        "border": "1px solid #CCC",
        "height": "30px",
        "padding": "10px 15px",
        "background": "#FAFAFA",
        "text-align": "center",
        "vertical-align": "middle"
    });


}

function chooseColor(color){
    var setcolor;
    switch (color){
        case 0: setcolor = 'Gray';
            break;
        case 1:setcolor = 'Red';
            break;
        case 2:setcolor = 'Blue';
            break;
        case 3:setcolor = 'Green';
            break;
        case 4:setcolor = 'Yellow';
            break;
        case 5:setcolor = 'Violet';
            break;
        case 6:setcolor = 'Pink';
            break;
        case 100:setcolor ='Marker';
            break;
    }
    return setcolor;
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





