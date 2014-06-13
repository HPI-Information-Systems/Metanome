function initialize() {

    totalConnections=0;
    renderLinks=[];
    fds=[];
    columns=[];
    conn=[];

    var root={};
    var o={};
    o.value=total_fds;
    o.children=fd_list;
    root.children=[o];

    nodes=bubble.nodes(root);

    var totalFdAmount=0;
    nodes.forEach (function (d) {
        if (d.depth==2) {
            nodesById[d.FD_ID]=d;
            d.relatedLinks=[];
            d.Amount=Number(100);
            d.currentAmount= d.Amount;
            fds.push(d);
            totalFdAmount+= d.Amount;
        }
    })

    log("totalFdAmount=" + totalFdAmount);
    columns=columnsfuncDep;
    c_funcDep.forEach(function (d) {
        conn.push(d);
    });

    buildChords();

    var totalconn=0;
    conn.forEach(function (d) {
        nodesById[d.FD_ID].relatedLinks.push(d);
        chordsById[d.COLUMN_ID].relatedLinks.push(d);
        totalconn+= Number(1);
    })

    log("totalConnections=" + totalconn);


    log("initialize()");

}
