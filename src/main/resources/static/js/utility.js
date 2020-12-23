function loadUrlInElementWithId(url, id) {
    $("#" + id).load(url);
    return false;
}

function highlightConfigurationOptions(cookieNames) {
    $.each(cookieNames, function (index, cookieName) {
        highlightConfigurationOption(cookieName)
    });
}

function highlightConfigurationOption(cookieName) {
    $("[cookieName='" + cookieName + "']").removeClass('select');
    $("[cookieName='" + cookieName + "'][cookieValue='" + getCookie(cookieName) + "']").addClass('select');
}

function getCookie(cookieName) {
    var name = cookieName + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            var value = c.substring(name.length, c.length);
            return value;
        }
    }
    return "";
}

function highLightSelectedOptions() {

}

function submitFormAndLoadResultInTarget(form, targetElement) {
    var button = $(event.target);
    button.attr("disabled", true);
    button.removeClass("btn-primary")
    button.addClass("btn-secondary")
    $.ajax({
        url: form.attr('action'),
        type: form.attr('method'),
        dataType: 'json',
        data: form.serialize(),
        success: function (result) {
            button.attr("disabled", false);
            button.removeClass("btn-secondary")
            button.addClass("btn-primary")
            var resultAsstring = "";
            $.each(result, function (i, rationalNumber) {
                resultAsstring += i + "\n" + rationalNumber + "\n\n";
            });
            targetElement.val(resultAsstring);
        },
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}
