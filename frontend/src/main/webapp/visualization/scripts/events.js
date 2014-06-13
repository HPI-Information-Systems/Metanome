
function node_onMouseOver(d,type) {
    if (type=="FD") {
        if(d.depth < 2) return;
        toolTip.transition()
            .duration(200)
            .style("opacity", ".9");

        header1.text("Functional Dependency")
        header.text(d.FD_ID);
        header2.text(d.FD_NAME);
        toolTip.style("left", (d3.event.pageX+15) + "px")
            .style("top", (d3.event.pageY-75) + "px")
            .style("height","100px");

        highlightLinks(d,true);
    }
    else if (type=="CONNECTION") {

        /*
        Highlight chord stroke
         */
        toolTip.transition()
            .duration(200)
            .style("opacity", ".9");

        header1.text("Functional Dependency")
        header.text(d.FD_ID + " -> " + columnsById[office + "_" + d.COLUMN_ID].COLUMN_NAME);
        header2.text(d.FD_ID + ": " + d.FD_NAME);
        toolTip.style("left", (d3.event.pageX+15) + "px")
            .style("top", (d3.event.pageY-75) + "px")
            .style("height","100px");
        highlightLink(d,true);
    }
    else if (type=="PAC") {
        /*
        highlight all connections and all FDidates
         */
        // toolTip.transition()
        //     .duration(200)
        //     .style("opacity", ".9");

        // header1.text("")
        // header.text(columnsById[office + "_" + d.label].COLUMN_NAME);
        // header2.text("");
        // toolTip.style("left", (d3.event.pageX+15) + "px")
        //     .style("top", (d3.event.pageY-75) + "px")
        //     .style("height","110px");
        highlightLinks(chordsById[d.label],true);
    }
}

function node_onMouseOut(d,type) {
    if (type=="FD") {
        highlightLinks(d,false);
    }
    else if (type=="CONNECTION") {
        highlightLink(d,false);
    }
    else if (type=="PAC") {
        highlightLinks(chordsById[d.label],false);
    }


    toolTip.transition()									// declare the transition properties to fade-out the div
        .duration(500)									// it shall take 500ms
        .style("opacity", "0");							// and go all the way to an opacity of nil

}

function highlightLink(g,on) {

    var opacity=((on==true) ? .6 : .1);

      // console.log("fadeHandler(" + opacity + ")");
      // highlightSvg.style("opacity",opacity);

       var link=d3.select(document.getElementById("l_" + g.Key));
        link.transition((on==true) ? 150:550)
            .style("fill-opacity",opacity)
            .style("stroke-opacity",opacity);

        var arc=d3.select(document.getElementById("a_" + g.Key));
        arc.transition().style("fill-opacity",(on==true) ? opacity :.2);

        var circ=d3.select(document.getElementById("c_" + g.FD_ID));
        circ.transition((on==true) ? 150:550)
        .style("opacity",((on==true) ?1 :0));

        var text=d3.select(document.getElementById("t_" + g.COLUMN_ID));
         text.transition((on==true) ? 0:550)
             .style("fill",(on==true) ? "#000" : "#777")
             .style("font-size",(on==true) ? "14px" : "12px")
             .style("stroke-width",((on==true) ? 2 : 0));


}

function highlightLinks(d,on) {

    d.relatedLinks.forEach(function (d) {
        highlightLink(d,on);
    })

}


senateButton.on("click",function (d) {

    senateButton.attr("class","selected");
    funcDepButton.attr("class",null);
    office="senate";
    linksSvg.selectAll("g.links").remove();
    clearInterval(intervalId);
    main();

});

funcDepButton.on("click",function (d) {
//    linkGroup.selectAll("g.links").remove();
    senateButton.attr("class",null);
    funcDepButton.attr("class","selected");
    office="funcDep";
//    linksSvg.selectAll("g.links").remove();
    clearInterval(intervalId);
    main();
});