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
            addToLog(e.responseText);
        },
        success: function (e) {
            addToLog('Observed Property created.');
            closeModal('dsdialog');
            getOprops();
        }
    });
}