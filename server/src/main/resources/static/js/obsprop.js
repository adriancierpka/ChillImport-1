function createObsprop() {


    var name = $("#obsname").val();
    var desc = $("#obsdescription").val();
    var def = $("#obsdefinition").val();

    var mydata = {
        name: name,
        description: desc,
        definition: def
    };

    $.ajax({
        type: "POST",
        url: "observedProperty/create",
        datatype: 'json',
        contentType: 'application/json',
        data: JSON.stringify(mydata),
        error: function (e) {
            $.notify({
                message: 'Observed Property could not be created, check the Log for errors'
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
            addToLog('Observed Property created.');
            closeModal('dsdialog');
            getOprops();
        }
    });
}