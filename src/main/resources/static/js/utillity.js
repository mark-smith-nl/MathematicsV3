function loadUrlInElementWithId(url, id) {
    $("#" + id).load(url);
    return false;
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