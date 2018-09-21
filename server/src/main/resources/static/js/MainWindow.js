/**
 * shows the popup to create a new Thing
 */
function showThingModal() {
    var $modal = $('#indexfooter').find('button:eq(0)');
    $modal.attr('onclick', 'createThing()');
    $modal.html('Create Thing');
    $modal.show();
    modal('dialog', 'thing.html', getLocations, 'Create a Thing');
}

/**
 * opens the popup to create a new datastream
 */
function showStreamModal() {
    var $modal = $('#indexfooter').find('button:eq(0)');
    $modal.attr('onclick', 'createDS()');
    $modal.html('Create Datastream');
    $modal.show();
    modal('dialog', 'datastream.html', initDatastream, 'Create a Datastream');
}

/**
 * opens the popup to edit the mapping
 */
function showMappingModal() {
    $('#indexfooter').find('button:eq(0)').hide();
    modal("dialog", "mapping.html", loadMapping, 'Current\xa0Mapping');
}

/**
 * opens the popup to show the failed rows
 */
function showReturnModal() {
    $('#indexfooter').find('button:eq(0)').hide();
    modal('dialog', 'returnRows.html', returnRows, 'Skipped Rows');
}

/**
 * gets all configs from the backend
 */
function loadConfigs() {
    $.ajax({
        type: "GET",
        url: "config/all",
        success: function (response) {
            var json = JSON.stringify(response, null, 4);
            var jsonparsed = JSON.parse(json);

            var list = $('#configs');
            list.empty().append(new Option('', '', null, null));
            for (var i = 0; i < jsonparsed.length; i++) {
                var option = new Option(jsonparsed[i].name, jsonparsed[i].name, null, null);
                option.setAttribute('data-value', JSON.stringify(jsonparsed[i], null, 4));
                list.append(option);
            }
            list.trigger('change');
        },
        error: function (e) {
            addToLog(e.responseText);
        }
    });
}

/**
 * saves a config on the server
 */
function saveConfig() {

    var cfgName = prompt('Name of Configuration:');
    if (cfgName === null) {
        return;
    }
    if (cfgName === "") {
        addToLog("A configuration needs a name");
        return;
    }
    var date = [];
    $('#timeTable').find('tbody tr').each(function () {
        var obj = {},
            $td = $(this).find('td');
        obj["string"] = $td.eq(1).find("input").val();
        obj["column"] = parseInt($td.eq(0).find("input").val());
        date.push(obj);
    });

    var streams = [];
    $('#datastreams').find('.datastream').each(function () {
        var obj = {},
            obs = [];
        obj['dsID'] = JSON.parse($(this).find('select option:selected').attr('data-value')).frostId;
        $(this).find('tbody tr').each(function () {
            obs.push(parseInt($(this).find('td:eq(1) input').val()));
        });
        obj['observations'] = obs;
        obj['multiStream'] = (obs.length > 1);
        streams.push(obj);
    });

    var filetype = null;
    if ($('input[name=source]:eq(0)').is(':checked')) {
        filetype = $('#file').val();
    } else if ($('input[name=source]:eq(1)').is(':checked')) {
        filetype = $('#sourceinput').val();
    }
    if (!(filetype) || filetype === "") {
        addToLog("No File specified.");
    } else {
        filetype = filetype.split('.').pop().toUpperCase();
        if (filetype === 'XLSX' || filetype === 'XLS') {
            filetype = 'EXCEL';
        } else if (filetype !== 'CSV') {
            addToLog("Unknown file type.");
            return;
        }
    }

    var map = mappingData;


    var formData = {
        name: cfgName,
        delimiter: $('#delimiter').val(),
        numberOfHeaderlines: parseInt($('#headerlines').val()),
        timezone: $('#selecttime option:selected').attr('data-value'),
        dateTime: date,
        streamData: streams,
        mapOfMagicNumbers: map,
        dataType: filetype
    };

    var jsoncfg = JSON.stringify(formData, null, 4);

    $.ajax({
            type: "POST",
            url: "config/create",
            contentType: "application/json",
            data: jsoncfg,
            dataType: 'json',
            error: function (e) {
                addToLog(e.responseText);
            },
            success: function (e) {
                var option = new Option(e.name, e.name, null, null);
                option.setAttribute('data-value', JSON.stringify(e, null, 4));
                $('#configs').append(option).val(e.name).trigger('change');
                addToLog("Configuration saved.");
            }
        }
    );
}

/**
 * loads the config with the id id
 * @param id
 */
function loadConfig(id) {

    $.ajax({
        type: "GET",
        url: "config/single",
        data: {
            configId: id
        },
        success: function (response) {
            mappingData = response.mapOfMagicNumbers;

            $('#delimiter').val(response.delimiter);
            $('#headerlines').val(response.numberOfHeaderlines);
            currentDelimiter = response.delimiter;
            currentHeaderLines = response.numberOfHeaderlines;
            preview();


            $('#selecttime').find('option').each(function () {
                if ($(this).attr('data-value') === response.timezone) {
                    $('#selecttime').val($(this).val()).trigger('change');
                }
            });

            var table = $('#timeTable');
            var lines = response.dateTime.length;
            var rows = document.getElementById('timeTable').rows.length - 1;
            while (lines > rows) {
                addRow(table, '2', '20');
                rows++;
            }
            while (lines < rows) {
                delLastRow('timeTable');
                rows--;
            }

            var i = 0;
            table.find('tbody tr').each(function () {
                var $td = $(this).find('td');
                $td.eq(0).find("input").val(response.dateTime[i].column);
                $td.eq(1).find("input").val(response.dateTime[i++].string);
            });


            lines = response.streamData.length;
            if (lines > 0) {
                thingConfig(response.streamData, null, true);
            }
        },
        error: function (e) {
            addToLog(e.responseText);
        }
    });
}

function thingConfig(streams) {

    $.ajax({
        type: 'GET',
        url: 'datastream/single',
        data: {
            id: parseInt(streams[0].dsID),
            isMulti: streams[0].multiStream
        },
        success: function (result) {
            loadThing(result.thing.frostId, streams);
        },
        error: function (e) {
            addToLog(e.responseText);
        }
    });
}

function loadStreamConfig(stream, div) {
    $.ajax({
        type: 'GET',
        url: 'datastream/single',
        data: {
            id: parseInt(stream.dsID),
            isMulti: stream.multiStream
        },
        success: function (result) {
            div.find('select').val(result.name + ' (' + result.frostId + ')').trigger('change');
            div.find('select option:selected').attr('data-value', JSON.stringify(result));
            loadStream(result, div);
            loadStreamCol(stream.observations, div);
        },
        error: function (e) {
            addToLog(e.responseText);
        }
    });
}

function fillStreams(streams) {
    var child;
    for (var i = 0; i < streams.length; i++) {
        addDatastream();
        child = $('#datastreams > div').last();
        loadStreamConfig(streams[i], child);
    }

}

/**
 * clears all inputs for the configuration
 */
function resetConfig() {
    document.getElementById("delimiter").value = ";";
    document.getElementById("headerlines").value = "0";
    $('#things').val(null).trigger('change');
    $('#selecttime').val(null).trigger('change');
    $('#timeTable').find('tbody').find('tr').each(function () {
        this.remove();
    });
    addRow($('#timeTable'), '2', '20');
    document.getElementById("datastreams").innerHTML = "";
    $('#configs').val(null).trigger('change');
    resetMapping();

    optimzeforsource(); //disables the delimiter and time format if the source file is an excel file
    showstep1();

}

/**
 * gets all things from the frost-server
 */
function getThings() {
    $.ajax({
        type: "GET",
        url: "thing/all",
        success: function (response) {
            var json = JSON.stringify(response, null, 4);
            var jsonparsed = JSON.parse(json);

            var list = $('#things');
            list.empty().append(new Option('', '', null, null));
            for (var i = 0; i < jsonparsed.length; i++) {
                var option = new Option(jsonparsed[i].name + ' (' + jsonparsed[i].frostId + ')', jsonparsed[i].name + ' (' + jsonparsed[i].frostId + ')', null, null);
                option.setAttribute('data-value', JSON.stringify(jsonparsed[i], null, 4));
                list.append(option);
            }
            list.trigger('change');
        },
        error: function (e) {
            addToLog(e.responseText);
        }
    });
}


var streamData = [];

/**
 * gets all datastreams from the thing with id id
 * @param id id of a thing
 */
function getThingStreams(id, cfg, streams) {
    $.ajax({
        type: "GET",
        url: "datastream/all",
        data: {
            thingId: id
        },
        success: function (response) {
            var json = JSON.stringify(response, null, 4);
            var jsonparsed = JSON.parse(json);

            streamData = [];
            var stream = {};
            stream['id'] = '';
            stream['text'] = '';
            streamData.push(stream);

            for (var i = 0; i < jsonparsed.length; i++) {
                stream = {};
                stream['id'] = jsonparsed[i].name + ' (' + jsonparsed[i].frostId + ')';
                stream['text'] = jsonparsed[i].name + ' (' + jsonparsed[i].frostId + ')';
                stream['data-value'] = JSON.stringify(jsonparsed[i], null, 4);
                streamData.push(stream);
            }

            $('#datastreams').empty();
            if (cfg) {
                fillStreams(streams);
            } else {
                addDatastream();
            }

        },
        error: function (e) {
            addToLog(e.responseText);
        }
    });
}

/**
 * deletes the stream "stream" from the stream-list in the config gui
 * @param stream
 */
function removeDatastream(stream) {
    stream.parentNode.removeChild(stream);
}

/**
 * adds an empty stream to the list in the config gui
 */
function addDatastream() {
    var streams = $('#datastreams');
    streams.append($('<div>')
        .attr('class', 'datastream')
        .append($('<div>')
            .append($('<label>')
                .text('Name:')
                .attr('style', 'margin-right:10px')
            )
            .append($('<select>')
                .attr('style', 'width: 200px')
                .attr('name', 'streams')
                .attr('class', 'selectStreams')
            )
            .append($('<button>')
                .attr('class', 'btn btn-default')
                .attr('onclick', 'removeDatastream(this.parentNode.parentNode)')
                .attr('style', 'width:auto; margin-left:10px')
                .html('<span class="glyphicon glyphicon-minus" ></span>')
            )
        )
        .append($('<div>')
            .attr('class', 'streamtable')
            .append($('<table>')
                .attr('class', 'table')
                .append($('<thead>')
                    .append($('<tr>')
                        .append($('<th>')
                            .text('Unit')
                        )
                        .append($('<th>')
                            .text('Column')
                        )
                    )
                )
                .append($('<tbody>')
                )
            )
        )
    );

    $('.selectStreams').last().select2({
        data: streamData,
        placeholder: 'Choose a Datastream',
        width: 'style',
        dropdownAutoWidth: true
    }).trigger('change').on('select2:select', function (e) {
        var sel = $(this);
        var parent = sel.parent().parent();
        var json;
        if (e.params.data['data-value']) {
            json = JSON.parse(e.params.data['data-value']);
            sel.find('option:selected').attr('data-value', JSON.stringify(json));
        } else {
            json = JSON.parse(sel.find('option:selected').attr('data-value'));
        }
        if (json) loadStream(json, parent.find('.streamtable'));
    });
    /*
    streams.find('.datastream').last().find('input').get(0).addEventListener('input', chooseStream);
    */
}

function showPreview() {
    alert('not implemented yet')
}

var id;
var retry = 0;
var initial = 1000;

/**
 * this function triggers the import
 */
function importData() {
    var name;
    if ($('input[name=source]:eq(0)').is(':checked')) {
        name = $('#file').val().split('\\').pop();
    } else if ($('input[name=source]:eq(1)').is(':checked')) {
        name = $('#sourceinput').val().split('/').pop();
    }

    var cfgName = "temp";
    var date = [];
    $('#timeTable').find('tbody tr').each(function () {
        var obj = {},
            $td = $(this).find('td');
        obj["string"] = $td.eq(1).find("input").val();
        obj["column"] = parseInt($td.eq(0).find("input").val());
        date.push(obj);
    });
    var streams = [];
    $('#datastreams').find('.datastream').each(function () {
        var obj = {},
            obs = [];
        obj['dsID'] = JSON.parse($(this).find('select option:selected').attr('data-value')).frostId;
        $(this).find('tbody tr').each(function () {
            obs.push(parseInt($(this).find('td:eq(1) input').val()));
        });
        obj['observations'] = obs;
        obj['multiStream'] = (obs.length > 1);
        streams.push(obj);
    });

    var filetype = null;
    if ($('input[name=source]:eq(0)').is(':checked')) {
        filetype = $('#file').val();
    } else if ($('input[name=source]:eq(1)').is(':checked')) {
        filetype = $('#sourceinput').val();
    }
    if (!(filetype) || filetype === "") {
        addToLog("No File specified.");
    } else {
        filetype = filetype.split('.').pop().toUpperCase();
        if (filetype === 'XLSX' || filetype === 'XLS') {
            filetype = 'EXCEL';
        } else if (filetype !== 'CSV') {
            addToLog("Unknown file type.");
            return;
        }
    }

    var map = mappingData;


    var formData = {
        name: cfgName,
        delimiter: $('#delimiter').val(),
        numberOfHeaderlines: parseInt($('#headerlines').val()),
        timezone: $('#selecttime option:selected').attr('data-value'),
        dateTime: date,
        streamData: streams,
        mapOfMagicNumbers: map,
        dataType: filetype
    };

    var jsoncfg = JSON.stringify(formData, null, 4);
    var mydata = {
        config: jsoncfg,
        filename: getCurrentFileName()
    };
    if (filetype == "EXCEL") {
        if (date.length == 0 || streams.length == 0 || $('#selecttime option:selected').attr('data-value') == null || isNaN(parseInt($('#headerlines').val()))) {
            $.notify({
                message: 'Please fill in all boxes'
            },{
                type: 'info'
            });
            return;
        }
    }
    if (filetype == "CSV") {
        if ($('#delimiter').val() == null || date.length == 0 || streams.length == 0 || $('#selecttime option:selected').attr('data-value') == null || isNaN(parseInt($('#headerlines').val()))) {
            $.notify({
                message: 'Please fill in all boxes'
            },{
                type: 'info',
                placement: {
                    from: "top",
                    align: "left"
                },
                animate: {
                    enter: 'animated fadeInDown',
                    exit: 'animated fadeOutUp'
                }
            });
            return;
        }
    }
    $.notify({
        message: 'Import started'
    },{
        type: 'info',
        allow_dismiss: true,
        placement: {
            from: "top",
            align: "left"
        },
        animate: {
            enter: 'animated fadeInDown',
            exit: 'animated fadeOutUp'
        }
    });
    addToLog("Import of File " + name + " started.");
    document.getElementById("progress").value = 0;
    id = setInterval(progress, initial);
    $.ajax({
        type: 'POST',
        url: "importQueue",
        data: mydata,
        success: function (e) {
            addToLog(e);
            $.notify({
                message: 'Import finished'
            },{
                type: 'success',
                allow_dismiss: true,
                placement: {
                    from: "top",
                    align: "left"
                },
                animate: {
                    enter: 'animated fadeInDown',
                    exit: 'animated fadeOutUp'
                }
            });
        },
        error: function (e) {
            $.notify({
                message: 'Import failed. Check log for errors.'
            },{
                type: 'danger',
                allow_dismiss: true,
                placement: {
                    from: "top",
                    align: "left"
                },
                animate: {
                    enter: 'animated fadeInDown',
                    exit: 'animated fadeOutUp'
                }
            });
            addToLog(e.responseText);
            clearInterval(id);
        }
    });
}

function progress() {
    $.ajax({
        type: 'GET',
        url: "progress",
        success: function (response) {
            var x = false;
            check(response, x);
            if (x) {
                return;
            }
            if (response == "Import has not started yet" || response == "File has not been converted yet") {
                initial = 2 * initial;
                if (initial >= 16000) {
                    addToLog("It seems the Import will take quite a while. Stopping progress requests.");
                    clearInterval(id);
                    return;
                }
                var temp = id;
                id = setInterval(progress, initial);
                clearInterval(temp);
            }
            if (response != "Finished") {
                addToLog(response);
                var resp = response;
                resp = resp.slice(-3);
                resp = resp.substring(0, 2);
                document.getElementById("progress").value = resp;
            }

        },
        error: function (e) {
            if (retry == 5) {
                addToLog("Progress could not be queried, stopping.");
                retry = 0;
                clearInterval(id);
                return;
            }
            retry++;
            addToLog("Could not get progress. Retrying " + 5 - retry + " more times");
        }
    });
}

function check(string, done) {
    if (string === 'Finished' || string.startsWith("Upload Queued")) {
        clearInterval(id);
        document.getElementById("progress").value = 100;
        done = true;
    }
}

/**
 * loads the data needed for the gui
 */
function main() {
    loadConfigs();
    document.getElementById("log").value = "";
    getThings();
    showFrostURL();
}

function pad2(number) {
    return (number < 10 ? '0' : '') + number
}

/**
 * adds a message to the log
 * @param msg the message
 */
function addToLog(msg) {
    var date = new Date();
    var time;
    hours = pad2(date.getHours());
    minutes = pad2(date.getMinutes());
    seconds = pad2(date.getSeconds());
    time = hours + ":" + minutes + ":" + seconds + "  ";
    document.getElementById("log").value += time + msg + "\n";

    showmessagetag();
}

/**
 * writes the observations in the gui
 * @param observations
 * @param div
 */
function loadStreamCol(observations, div) {
    for (var i = 0; i < observations.length; i++) {
        div.find('tbody tr').eq(i).find('td input').val(observations[i]);
    }
}

/**
 * loads the streamdata from the server
 * @param dsid
 * @param multi
 * @param div
 * @param cfg
 */
function loadStreamData(stream, div) {
    $.ajax({
        type: 'GET',
        url: 'datastream/single',
        data: {
            id: parseInt(stream.dsID),
            isMulti: stream.multiStream
        },
        success: function (result) {
            loadStream(result, div);
        },
        error: function (e) {
            addToLog(e.responseText);
        }
    });
}

function loadThing(id, streams) {
    $.ajax({
        type: 'GET',
        url: 'thing/single',
        data: {
            thingId: parseInt(id)
        },
        success: function (result) {
            $('#things').val(result.name + " (" + result.frostId + ")").trigger('change');
            getThingStreams(result.frostId, true, streams);
        },
        error: function (e) {
            addToLog(e.responseText);
        }
    });
}


function loadStream(stream, div) {
    var rows = stream.units_of_measurement.length;
    $(div).find('tbody').empty();
    for (var i = 0; i < rows; i++) {
        $(div).find('tbody')
            .append($('<tr>')
                .append($('<td>')
                    .text(stream.units_of_measurement[i].symbol)
                )
                .append($('<td>')
                    .append($('<input>')
                        .attr('type', 'text')
                        .attr('size', '5')
                    )
                )
            );
    }
}

function getThing() {
    return JSON.parse($('#things option:selected').attr('data-value'));

}

/**
 * This function is called if a configuration is selected
 * @param e a config
 */
$('#configs').on('select2:select', function (e) {
    var json = JSON.parse($('#configs option:selected').attr('data-value'));
    if (json) {
        loadConfig(json.id);
    }
    openaccordion("currentConfigAcc");
});

$('#things').on('select2:select', function (e) {
    var json = getThing();
    if (json) {
        getThingStreams(json.frostId);
    }
});


/**
 * gets the frost url from the backend and shows it
 */
function showFrostURL() {
    $.ajax({
        type: 'GET',
        url: "getfrosturl",
        success: function (response) {
            document.getElementById("serverurlbox").innerText = response;
            document.getElementById("serverurlbox").href = response;
            addToLog("FROST-Server: " + response);
        },
        error: function (e) {
            addToLog(e.responseText);
        }

    });
}

/**
 * function performed after choosing a sourcefile. Uploads file (and triggers preview afterwards), and opens next part of the accordion (choice of configuration)
 */
function fileConfirmed() {
    upload();
    optimzeforsource();
    openaccordion('chooseConfigAcc');
}