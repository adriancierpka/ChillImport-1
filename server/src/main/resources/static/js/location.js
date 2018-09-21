function createLocation() {

    var name = $("#locname").val();
    var desc = $("#locdescription").val();
    var location = "[" + $("#loclocation").val() + "]";

    var mydata = {
        name: name,
        description: desc,
        encoding_TYPE: 'application/vnd.geo+json',
        location: "{\"type\": \"Point\", \"coordinates\": " + location + "}"
    };

    $.ajax({
            type: "POST",
            url: "location/create",
            datatype: 'json',
            contentType: 'application/json',
            data: JSON.stringify(mydata),
            error: function (e) {
                addToLog(e.responseText);
            },
            success: function (e) {
                addToLog("Location created.");
                closeModal('thingdialog');

                var text = e.name + ' (' + e.frostId + ')';
                var option = new Option(text, text, null, null);
                option.setAttribute('data-value', JSON.stringify(e, null, 4));

                $('#locations').append(option).trigger('change');
                $('#locations').val(text);
            }
        }
    )
    ;
}