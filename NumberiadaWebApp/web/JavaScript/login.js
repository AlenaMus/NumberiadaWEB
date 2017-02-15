
$(function () {
    checkSessionExist();
    initilazeLoginButton();
    $("body").hide();
});

function initilazeLoginButton() {
    $("button#LogIn").click(function () {
        $("#Error").hide();
        var username = $("#usernameInput").val();
        if (username === undefined || username.trim() === "") {
            $("#Error").text("Username must be at least 1 character.").show();
        }
        else {
            initilazeLogin();
        }
        return false;
    });
}

function initilazeLogin()
{
    var userNameInput = $("#usernameInput").val();
    var isComputer = $("#isComputer").val();
    $.ajax({
        type: 'POST',
        data: {username:userNameInput,isComputer:isComputer },
        url: "login",
        timeout: 6000,
        success: function (data, textStatus, jqXHR) {
            if (data.loginSuccess === true) {
                window.location.href = "LobbyPage.html";
            }
            else {
                if (data.usernameExists === true) {
                    $("#Error").text("Someone already has that username. Try another?").show();
                }
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (textStatus === "timeout") {
                $("#Error").text("Server Timeout. Try again..").show();
            }
            else {
                $("#Error").text("Something went wrong. Try again..").show();
            }
            console.error(errorThrown.toString());
        }
    });
}

function checkSessionExist() {
    $.ajax({
        type: 'POST',
        url: "CheckLogIn",
        timeout: 6000,
        success: function (login, textStatus, jqXHR) {
            if (login === "true") {
                window.location.href = "LobbyPage.html";
            }
            else {
                $("body").show();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $("#Error").text("Something went wrong. Try again..").show();
            console.error(errorThrown.toString());
        }
    });
}

