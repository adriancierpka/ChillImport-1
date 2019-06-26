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
                $.notify({
                    message: 'Location could not be created, check the Log for errors'
                }, {
                    allow_dismiss: true,
                    type: 'danger',
                    placement: {
                        from: "top",
                        align: "left"
                    },
                    animate: {
                        enter: 'animated fadeInDown',
                        exit: 'animated fadeOutUp'
                    },
                    z_index: 9000
                });
                addToLog(e.responseText);
            },
            success: function (e) {
                $.notify({
                    message: 'Location created.'
                },{
                    allow_dismiss:true,
                    type: 'info',
                    placement: {
                        from: "top",
                        align: "left"
                    },
                    animate: {
                        enter: 'animated fadeInDown',
                        exit: 'animated fadeOutUp'
                    },
                    z_index: 9000
                });
                addToLog('Location created.');
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