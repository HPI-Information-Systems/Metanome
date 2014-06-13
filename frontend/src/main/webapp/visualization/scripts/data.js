
/**
 *
 * DATA SOURCE:  http://www.fec.gov/data/index.jsp/
 *
 */

/**
 * Daisy Chain Data Fetches to ensure all data is loaded prior to updates (async calls)
 */

// var dataDispatch=d3.dispatch("end");

function fetchData() {
    dataCalls=[];
//    dataDispatch.on("end",onDataFetched)
    addStream("visulaization/data/FunctionalDependencies.csv", onFetchFunctionalDependencies);
    addStream("visulaization/data/Connection.csv", onFetchConnection);
    addStream("visulaization/data/Column.csv", onFetchColumn);
    startFetch();
}

function onFetchFunctionalDependencies(csv) {
    for (var i=0; i < csv.length; i++) {
        var r=csv[i];
        r.value=Number(1);
        cns[r.FD_ID]=r;
        funcDep.push(r);
        fd_list.push(r);
        total_fds+= r.value;
    }
    log("onFetchFunctionalDependencies()");
    endFetch();
}

function onFetchConnection(csv) {
    var i=0;
    csv.forEach(function (d) {
        d.Key="H"+(i++);
        connections.push(d);
        c_funcDep.push(d);
    });

    log("onFetchConnection()");
    endFetch();

}

function onFetchColumn(csv) {

    columnsfuncDep=csv;
    for (var i=0; i < columnsfuncDep.length; i++) {
        columnsById["funcDep_" + columnsfuncDep[i].COLUMN_ID]=columnsfuncDep[i];
    }

    log("onFetchColumn()");
    endFetch();


}


function addStream(file,func) {
    var o={};
    o.file=file;
    o.function=func;
    dataCalls.push(o);
}

function startFetch() {
    numCalls=dataCalls.length;
    dataCalls.forEach(function (d) {
        d3.csv(d.file, d.function);
    })
}

function endFetch() {
    numCalls--;
    if (numCalls==0) {
       // dataDispatch.end();
        main();
    }
}
