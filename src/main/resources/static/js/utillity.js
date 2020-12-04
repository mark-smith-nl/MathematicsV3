function loadUrlInElementWithId(url, id) {
    $("#" + id).load(url);
    return false;
}

function setConstantAsCookie(cookieName, cookieValue) {
    var expires = "";
    var experationDate = new Date();
    experationDate.setTime(experationDate.getTime() + 365 * 24 * 60 * 60 * 1000);
    document.cookie = cookieName + "=" + cookieValue + "; expires=" + experationDate.toUTCString();
    highLightOption(cookieName);
    return false;
}

function highLightOptions(cookieNames) {
    $.each(cookieNames, function (index, cookieName) {
        highLightOption(cookieName)
    });
}

function highLightOption(cookieName) {
    $("[cookieName='" + cookieName + "']").removeClass('asteriskMarkAfter');
    $("[cookieName='" + cookieName + "'][cookieValue='" + getCookie(cookieName) + "']").addClass('asteriskMarkAfter');
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
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function highLightSelectedOptions() {

}

function submitFormAndLoadResultInTarget(form, targetElement) {
    $.ajax({
        url: form.attr('action'),
        type: form.attr('method'),
        dataType: 'json',
        data: form.serialize(),
        success: function (result) {
            var resultAsstring = "";
            $.each(result, function (i, rationalNumber) {
                resultAsstring += i + "\n" + rationalNumber + "\n\n";
            });
            alert(resultAsstring)
            targetElement.val(resultAsstring);
        },
        error: function (xhr, resp, text) {
            console.log(xhr, resp, text);
        }
    });
}


