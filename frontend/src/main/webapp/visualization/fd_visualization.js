function draw_fd(svgDiv, mainDiv, bpgDiv, toolTipDiv, header1Div, header2Div, headDiv, $doc) {

    /*
     GLOBAL
     */

    var maxWidth = Math.max(600, Math.min(screen.height, screen.width) - 250);

    var outerRadius = maxWidth / 2,
        innerRadius = outerRadius - 120,
        bubbleRadius = innerRadius - 50,
        linkRadius = innerRadius - 20,
        nodesTranslate = (outerRadius - innerRadius) + (innerRadius - bubbleRadius) + 100,
        chordsTranslate = (outerRadius + 100);

    d3.select(mainDiv)
        .style("width",(outerRadius*2 + 400) + "px")
        .style("height",(outerRadius*2 + 400) + "px");

    d3.select(bpgDiv)
        .style("width",(outerRadius*2 + 400) + "px");

    var svg = d3.select(svgDiv)
        .style("width", (outerRadius * 2 + 200) + "px")
        .style("height", (outerRadius * 2 + 200) + "px")
        .append("svg")
        .attr("id", "svg")
        .style("width", (outerRadius * 2 + 200) + "px")
        .style("height", (outerRadius * 2 + 200) + "px");

    var chordsSvg = svg.append("g")
        .attr("class", "chords")
        .attr("transform", "translate(" + chordsTranslate + "," + chordsTranslate + ")");

    var linksSvg = svg.append("g")
        .attr("class", "links")
        .attr("transform", "translate(" + chordsTranslate + "," + chordsTranslate + ")");

    var highlightSvg = svg.append("g")
        .attr("transform", "translate(" + chordsTranslate + "," + chordsTranslate + ")")
        .style("opacity", 0);

    var nodesSvg = svg.append("g")
        .attr("class", "nodes")
        .attr("transform", "translate(" + nodesTranslate + "," + nodesTranslate + ")");

    var bubble = d3.layout.pack()
        .sort(null)
        .size([bubbleRadius * 2, bubbleRadius * 2])
        .padding(1.5);

    var chord = d3.layout.chord()
        .padding(.05)
        .sortSubgroups(d3.descending)
        .sortChords(d3.descending);

    var diagonal = d3.svg.diagonal.radial();

    var arc = d3.svg.arc()
        .innerRadius(innerRadius)
        .outerRadius(innerRadius + 10);

    var diameter = 960,
        format = d3.format(",d"),
        color = d3.scale.category20c();

    var toolTip = d3.select(toolTipDiv);
    var header = d3.select(headDiv);
    var header1 = d3.select(header1Div);
    var header2 = d3.select(header2Div);
    var total = d3.select($doc.getElementById("totalDiv"));
    var repColor = "#F80018";
    var color = "#0543bc";
    var otherColor = "#FFa400";

    var fills = d3.scale.ordinal().range(["#00AC6B", "#20815D", "#007046", "#35D699", "#60D6A9"]);

    var office = "funcDep";

    var linkGroup;

    var cns = [],
        fds = [],
        columns = [],
        columnsfuncDep = [],
        columnsSentate = [],
        conn = [],
        h_dems = [],
        h_reps = [],
        fd_list = [],
        funcDep = [];
    s_dems = [],
        s_reps = [],
        s_others = [],
        senate = [],
        total_hDems = 0,
        total_sDems = 0,
        total_hReps = 0,
        total_sReps = 0,
        total_fds = 0,
        total_sOthers = 0,
        connections = [],
        c_senate = [];
    c_funcDep = [];
    columns = [],
        columnsById = {},
        chordsById = {},
        nodesById = {},
        chordCount = 20,
        pText = null,
        pChords = null,
        nodes = [],
        renderLinks = [],
        colorByName = {},
        totalConnections = 0,
        delay = 2;

    var formatNumber = d3.format(",.0f"),
        formatCurrency = function (d) {
            return "$" + formatNumber(d)
        };

    var buf_indexByName = {},
        indexByName = {},
        nameByIndex = {},
        labels = [],
        chords = [];


    /*
     DATA FETCHING
     */

    var dataCalls = [];
    var numCalls = 0;

    function fetchData() {
        dataCalls = [];
        addStream("visualization/data/FunctionalDependencies.csv", onFetchFunctionalDependencies);
        addStream("visualization/data/Connection.csv", onFetchConnection);
        addStream("visualization/data/Column.csv", onFetchColumn);
        startFetch();
    }

    function onFetchFunctionalDependencies(csv) {
        for (var i = 0; i < csv.length; i++) {
            var r = csv[i];
            r.value = Number(1);
            cns[r.FD_ID] = r;
            funcDep.push(r);
            fd_list.push(r);
            total_fds += r.value;
        }
        endFetch();
    }

    function onFetchConnection(csv) {
        var i = 0;
        csv.forEach(function (d) {
            d.Key = "H" + (i++);
            connections.push(d);
            c_funcDep.push(d);
        });
        endFetch();
    }

    function onFetchColumn(csv) {
        columnsfuncDep = csv;
        for (var i = 0; i < columnsfuncDep.length; i++) {
            columnsById["funcDep_" + columnsfuncDep[i].COLUMN_ID] = columnsfuncDep[i];
        }
        endFetch();
    }

    function addStream(file, func) {
        var o = {};
        o.file = file;
        o.function = func;
        dataCalls.push(o);
    }

    function startFetch() {
        numCalls = dataCalls.length;
        dataCalls.forEach(function (d) {
            d3.csv(d.file, d.function);
        })
    }

    function endFetch() {
        numCalls--;
        if (numCalls == 0) {
            main();
        }
    }


    /*
     MAIN
     */

    fetchData();

    var intervalId;
    var counter = 2;
    var renderLinks = [];

    function main() {
        initialize();
        updateNodes();
        updateChords();
        intervalId = setInterval(onInterval, 1);
    }

    function onInterval() {
        if (conn.length == 0) {
            clearInterval(intervalId);
        }
        else {
            for (var i = 0; i < counter; i++) {
                if (conn.length > 0) {
                    renderLinks.push(conn.pop());
                }
            }
            counter = 30;
            updateLinks(renderLinks);
        }
    }


    /*
     INITIALIZE
     */

    function initialize() {

        totalConnections = 0;
        renderLinks = [];
        fds = [];
        columns = [];
        conn = [];

        var root = {};
        var o = {};
        o.value = total_fds;
        o.children = fd_list;
        root.children = [o];

        nodes = bubble.nodes(root);

        var totalFdAmount = 0;
        nodes.forEach(function (d) {
            if (d.depth == 2) {
                nodesById[d.FD_ID] = d;
                d.relatedLinks = [];
                d.Amount = Number(100);
                d.currentAmount = d.Amount;
                fds.push(d);
                totalFdAmount += d.Amount;
            }
        })

        columns = columnsfuncDep;
        c_funcDep.forEach(function (d) {
            conn.push(d);
        });

        buildChords();

        var totalconn = 0;
        conn.forEach(function (d) {
            nodesById[d.FD_ID].relatedLinks.push(d);
            chordsById[d.COLUMN_ID].relatedLinks.push(d);
            totalconn += Number(1);
        })
    }


    /*
     EVENTS
     */

    function node_onMouseOver(d, type) {
        if (type == "FD") {
            if (d.depth < 2) return;
            toolTip.transition()
                .duration(200)
                .style("opacity", ".9");

            header1.text("Functional Dependency")
            header.text(d.FD_ID);
            header2.text(d.FD_NAME);
            toolTip.style("left", (d3.event.pageX + 15) + "px")
                .style("top", (d3.event.pageY - 75) + "px")
                .style("height", "100px");

            highlightLinks(d, true);
        }
        else if (type == "CONNECTION") {
            toolTip.transition()
                .duration(200)
                .style("opacity", ".9");

            header1.text("Functional Dependency")
            header.text(d.FD_ID + " -> " + columnsById[office + "_" + d.COLUMN_ID].COLUMN_NAME);
            header2.text(d.FD_ID + ": " + d.FD_NAME);
            toolTip.style("left", (d3.event.pageX + 15) + "px")
                .style("top", (d3.event.pageY - 75) + "px")
                .style("height", "100px");
            highlightLink(d, true);
        }
        else if (type == "PAC") {
            highlightLinks(chordsById[d.label], true);
        }
    }

    function node_onMouseOut(d, type) {
        if (type == "FD") {
            highlightLinks(d, false);
        }
        else if (type == "CONNECTION") {
            highlightLink(d, false);
        }
        else if (type == "PAC") {
            highlightLinks(chordsById[d.label], false);
        }

        toolTip.transition()                                    // declare the transition properties to fade-out the div
            .duration(500)                                  // it shall take 500ms
            .style("opacity", "0");                         // and go all the way to an opacity of nil
    }

    function highlightLink(g, on) {

        var opacity = ((on == true) ? .6 : .1);

        var link = d3.select($doc.getElementById("l_" + g.Key));
        link.transition((on == true) ? 150 : 550)
            .style("fill-opacity", opacity)
            .style("stroke-opacity", opacity);

        var arc = d3.select($doc.getElementById("a_" + g.Key));
        arc.transition().style("fill-opacity", (on == true) ? opacity : .2);

        var circ = d3.select($doc.getElementById("c_" + g.FD_ID));
        circ.transition((on == true) ? 150 : 550)
            .style("opacity", ((on == true) ? 1 : 0));

        var text = d3.select($doc.getElementById("t_" + g.COLUMN_ID));
        text.transition((on == true) ? 0 : 550)
            .style("fill", (on == true) ? "#000" : "#777")
            .style("font-size", (on == true) ? "14px" : "12px")
            .style("stroke-width", ((on == true) ? 2 : 0));


    }

    function highlightLinks(d, on) {

        d.relatedLinks.forEach(function (d) {
            highlightLink(d, on);
        })

    }


    /*
     UPDATE
     */

    function updateLinks(links) {

        linkGroup = linksSvg.selectAll("g.links")
            .data(links, function (d, i) {
                return d.Key;
            });

        var enter = linkGroup.enter().append("g").attr("class", "links");
        var update = linkGroup.transition();

        enter.append("g")
            .attr("class", "arc")
            .append("path")
            .attr("id", function (d) {
                return "a_" + d.Key;
            })
            .style("fill", function (d) {
                return color;
            })
            .style("fill-opacity", .2)
            .attr("d", function (d, i) {
                var newArc = {};
                var relatedChord = chordsById[d.COLUMN_ID];
                newArc.startAngle = relatedChord.currentAngle;
                relatedChord.currentAngle = relatedChord.currentAngle + (Number(1) / relatedChord.value) * (relatedChord.endAngle - relatedChord.startAngle);
                newArc.endAngle = relatedChord.currentAngle;
                newArc.value = Number(1);
                var arc = d3.svg.arc(d, i).innerRadius(linkRadius).outerRadius(innerRadius);
                totalConnections += newArc.value;
                total.text(formatCurrency(totalConnections));

                return arc(newArc, i);
            })
            .on("mouseover", function (d) {
                node_onMouseOver(d, "CONNECTION");
            })
            .on("mouseout", function (d) {
                node_onMouseOut(d, "CONNECTION");
            });

        /* LINKS */
        enter.append("path")
            .attr("class", "link")
            .attr("id", function (d) {
                return "l_" + d.Key;
            })
            .attr("d", function (d, i) {
                d.links = createLinks(d);
                var diag = diagonal(d.links[0], i);
                diag += "L" + String(diagonal(d.links[1], i)).substr(1);
                diag += "A" + (linkRadius) + "," + (linkRadius) + " 0 0,0 " + d.links[0].source.x + "," + d.links[0].source.y;
                1

                return diag;
            })
            .style("stroke", function (d) {
                return color;
            })
            .style("stroke-opacity", .07)
            .style("fill-opacity", 0.1)
            .style("fill", function (d) {
                return color;
            })
            .on("mouseover", function (d) {
                node_onMouseOver(d, "CONNECTION");
            })
            .on("mouseout", function (d) {
                node_onMouseOut(d, "CONNECTION");
            });

        /* NODES */
        enter.append("g")
            .attr("class", "node")
            .append("circle")
            .style("fill", function (d) {
                return color;
            })
            .style("fill-opacity", 0.2)
            .style("stroke-opacity", 1)
            .attr("r", function (d) {
                var relatedNode = nodesById[d.FD_ID];
                relatedNode.currentAmount = relatedNode.currentAmount - Number(20);
                var ratio = ((relatedNode.Amount - relatedNode.currentAmount) / relatedNode.Amount);
                return relatedNode.r * ratio;
            })
            .attr("transform", function (d, i) {
                return "translate(" + (d.links[0].target.x) + "," + (d.links[0].target.y) + ")";
            })

        linkGroup.exit().remove();


        function createLinks(d) {
            var target = {};
            var source = {};
            var link = {};
            var link2 = {};
            var source2 = {};

            var relatedChord = chordsById[d.COLUMN_ID];
            var relatedNode = nodesById[d.FD_ID];
            var r = linkRadius;
            var currX = (r * Math.cos(relatedChord.currentLinkAngle - 1.57079633));
            var currY = (r * Math.sin(relatedChord.currentLinkAngle - 1.57079633));

            var a = relatedChord.currentLinkAngle - 1.57079633; //-90 degrees
            relatedChord.currentLinkAngle = relatedChord.currentLinkAngle + (Number(1) / relatedChord.value) * (relatedChord.endAngle - relatedChord.startAngle);
            var a1 = relatedChord.currentLinkAngle - 1.57079633;

            source.x = (r * Math.cos(a));
            source.y = (r * Math.sin(a));
            target.x = relatedNode.x - (chordsTranslate - nodesTranslate);
            target.y = relatedNode.y - (chordsTranslate - nodesTranslate);
            source2.x = (r * Math.cos(a1));
            source2.y = (r * Math.sin(a1));
            link.source = source;
            link.target = target;
            link2.source = target;
            link2.target = source2;

            return [link, link2];

        }
    }

    function updateNodes() {

        var node = nodesSvg.selectAll("g.node")
            .data(fds, function (d, i) {
                return d.FD_ID;
            });

        var enter = node.enter().append("g")
            .attr("class", "node")
            .attr("transform", function (d) {
                return "translate(" + d.x + "," + d.y + ")";
            });

        enter.append("circle")
            .attr("r", function (d) {
                return d.r;
            })
            .style("fill-opacity", function (d) {
                return (d.depth < 2) ? 0 : 0.05
            })
            .style("stroke", function (d) {
                return color;
            })
            .style("stroke-opacity", function (d) {
                return (d.depth < 2) ? 0 : 0.2
            })
            .style("fill", function (d) {
                return color;
            });

        var g = enter.append("g")
            .attr("id", function (d) {
                return "c_" + d.FD_ID;
            })
            .style("opacity", 0);

        g.append("circle")
            .attr("r", function (d) {
                return d.r + 2;
            })
            .style("fill-opacity", 0)
            .style("stroke", "#FFF")
            .style("stroke-width", 2.5)
            .style("stroke-opacity", .7);

        g.append("circle")
            .attr("r", function (d) {
                return d.r;
            })
            .style("fill-opacity", 0)
            .style("stroke", "#000")
            .style("stroke-width", 1.5)
            .style("stroke-opacity", 1)
            .on("mouseover", function (d) {
                node_onMouseOver(d, "FD");
            })
            .on("mouseout", function (d) {
                node_onMouseOut(d, "FD");
            });

        node.exit().remove().transition(500).style("opacity", 0);
    }

    function updateChords() {
        var arcGroup = chordsSvg.selectAll("g.arc")
            .data(chords, function (d) {
                return d.label;
            });

        var enter = arcGroup.enter().append("g").attr("class", "arc");

        enter.append("text")
            .attr("class", "chord")
            .attr("dy", ".35em")
            .attr("text-anchor", function (d) {
                return d.angle > Math.PI ? "end" : null;
            })
            .attr("transform", function (d) {
                return "rotate(" + (d.angle * 180 / Math.PI - 90) + ")"
                    + "translate(" + (innerRadius + 6) + ")"
                    + (d.angle > Math.PI ? "rotate(180)" : "");
            })
            .text(function (d) {
                return trimLabel(columnsById[office + "_" + d.label].COLUMN_NAME);
            })
            .on("mouseover", function (d) {
                node_onMouseOver(d, "PAC");
            })
            .on("mouseout", function (d) {
                node_onMouseOut(d, "PAC");
            });

        arcGroup.transition()
            .select("text")
            .attr("id", function (d) {
                return "t_" + d.label;
            })
            .attr("dy", ".35em")
            .attr("text-anchor", function (d) {
                return d.angle > Math.PI ? "end" : null;
            })
            .attr("transform", function (d) {
                return "rotate(" + (d.angle * 180 / Math.PI - 90) + ")"
                    + "translate(" + (innerRadius + 6) + ")"
                    + (d.angle > Math.PI ? "rotate(180)" : "");
            })
            .style("fill", "#777")
            .text(function (d) {
                return trimLabel(columnsById[office + "_" + d.label].COLUMN_NAME);
            });

        enter.append("path")
            .style("fill-opacity", 0)
            .style("stroke", "#555")
            .style("stroke-opacity", 0.4)
            .attr("d", function (d, i) {
                var arc = d3.svg.arc(d, i).innerRadius(innerRadius - 20).outerRadius(innerRadius);
                return arc(d.source, i);
            });

        arcGroup.transition()
            .select("path")
            .attr("d", function (d, i) {
                var arc = d3.svg.arc(d, i).innerRadius(innerRadius - 20).outerRadius(innerRadius);
                return arc(d.source, i);
            });


        arcGroup.exit().remove();
    }

    function trimLabel(label) {
        if (label.length > 25) {
            return String(label).substr(0, 25) + "...";
        }
        else {
            return label;
        }
    }

    function getChordColor(i) {
        var country = nameByIndex[i];
        if (colorByName[country] == undefined) {
            colorByName[country] = fills(i);
        }

        return colorByName[country];
    }


    /*
     BUILD CHORDS
     */

    function buildChords() {

        var matrix = [];
        labels = [];
        chords = [];

        for (var i = 0; i < columns.length; i++) {
            var l = {};
            l.index = i;
            l.label = "null";
            l.angle = 0;
            labels.push(l);

            var c = {}
            c.label = "null";
            c.source = {};
            c.target = {};
            chords.push(c);

        }

        buf_indexByName = indexByName;

        indexByName = [];
        nameByIndex = [];
        n = 0;

        var totalPacAmount = 0;

        // Compute a unique index for each package name
        columns.forEach(function (d) {
            d = d.COLUMN_ID;
            if (!(d in indexByName)) {
                nameByIndex[n] = d;
                indexByName[d] = n++;
            }
        });

        columns.forEach(function (d) {
            var source = indexByName[d.COLUMN_ID],
                row = matrix[source];
            if (!row) {
                row = matrix[source] = [];
                for (var i = -1; ++i < n;) row[i] = 0;
            }
            row[indexByName[d.COLUMN_ID]] = Number(d.Amount);
            totalPacAmount += Number(d.Amount);
        });

        chord.matrix(matrix);

        var tempLabels = [];
        var tempChords = [];

        chords = chord.chords();

        var i = 0;
        chords.forEach(function (d) {
            d.label = nameByIndex[i];
            d.angle = (d.source.startAngle + d.source.endAngle) / 2
            var o = {};
            o.startAngle = d.source.startAngle;
            o.endAngle = d.source.endAngle;
            o.index = d.source.index;
            o.value = d.source.value;
            o.currentAngle = d.source.startAngle;
            o.currentLinkAngle = d.source.startAngle;
            o.Amount = d.source.value;
            o.source = d.source;
            o.relatedLinks = [];
            chordsById[d.label] = o;
            i++;
        });

        function getFirstIndex(index, indexes) {
            for (var i = 0; i < chordCount; i++) {
                var found = false;
                for (var y = index; y < indexes.length; y++) {
                    if (i == indexes[y]) {
                        found = true;
                    }
                }
                if (found == false) {
                    return i;
                    //  break;
                }
            }
        }

        function getLabelIndex(name) {
            for (var i = 0; i < chordCount; i++) {
                if (buffer[i].label == name) {
                    return i;
                    //   break;
                }
            }
            return -1;
        }
    }

}