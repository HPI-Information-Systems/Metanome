
var maxWidth=Math.max(600,Math.min(screen.height,screen.width)-250);

var outerRadius = maxWidth / 2,
    innerRadius = outerRadius - 120,
    bubbleRadius=innerRadius-50,
    linkRadius=innerRadius-20,
    nodesTranslate=(outerRadius-innerRadius) + (innerRadius-bubbleRadius) + 100,
    chordsTranslate=(outerRadius + 100);

var funcDepButton=d3.select(document.getElementById("funcDepButton"));
var senateButton=d3.select(document.getElementById("senateButton"));

d3.select(document.getElementById("mainDiv"))
    .style("width",(outerRadius*2 + 400) + "px")
    .style("height",(outerRadius*2 + 400) + "px");

d3.select(document.getElementById("bpg"))
    .style("width",(outerRadius*2 + 400) + "px");

var svg = d3.select(document.getElementById("svgDiv"))
    .style("width", (outerRadius*2 + 200) + "px")
    .style("height", (outerRadius*2 + 200) + "px")
    .append("svg")
    .attr("id","svg")
    .style("width", (outerRadius*2 + 200) + "px")
    .style("height", (outerRadius*2 + 200) + "px");



var chordsSvg=svg.append("g")
    .attr("class","chords")
    .attr("transform", "translate(" + chordsTranslate + "," + chordsTranslate + ")");


var linksSvg=svg.append("g")
    .attr("class","links")
    .attr("transform", "translate(" + chordsTranslate + "," + chordsTranslate + ")")


var highlightSvg=svg.append("g")
    .attr("transform", "translate(" + chordsTranslate + "," + chordsTranslate + ")")
    .style("opacity",0);

var highlightLink=highlightSvg.append("path");

var nodesSvg=svg.append("g")
    .attr("class","nodes")
    .attr("transform", "translate(" + nodesTranslate + "," + nodesTranslate + ")");


 var bubble = d3.layout.pack()
    .sort(null)
    .size([bubbleRadius*2, bubbleRadius*2])
    .padding(1.5);

var chord = d3.layout.chord()
    .padding(.05)
    .sortSubgroups(d3.descending)
    .sortChords(d3.descending);

var diagonal = d3.svg.diagonal.radial();
    //.projection(function(d) { return [d.y, d.x / 180 * Math.PI]; });


var arc = d3.svg.arc()
    .innerRadius(innerRadius)
    .outerRadius(innerRadius + 10);


var diameter = 960,
    format = d3.format(",d"),
    color = d3.scale.category20c();

var toolTip = d3.select(document.getElementById("toolTip"));
var header = d3.select(document.getElementById("head"));
var header1 = d3.select(document.getElementById("header1"));
var header2 = d3.select(document.getElementById("header2"));
var total = d3.select(document.getElementById("totalDiv"));
var repColor="#F80018";
var color="#0543bc";
var otherColor="#FFa400";

var fills= d3.scale.ordinal().range(["#00AC6B","#20815D","#007046","#35D699","#60D6A9"]);

var office="funcDep";

var linkGroup;

var cns=[],
    fds=[],
    columns=[],
    columnsfuncDep=[],
    columnsSentate=[],
    conn=[],
    h_dems=[],
    h_reps=[],
    fd_list=[],
    funcDep=[];
    s_dems=[],
    s_reps=[],
    s_others=[],
    senate=[],
    total_hDems=0,
    total_sDems=0,
    total_hReps=0,
    total_sReps=0,
    total_fds=0,
    total_sOthers=0,
    connections=[],
    c_senate=[];
    c_funcDep=[];
    columns=[],
    columnsById={},
    chordsById={},
    nodesById={},
    chordCount=20,
    pText=null,
    pChords=null,
    nodes=[],
    renderLinks=[],
    colorByName={},
    totalConnections=0,
    delay=2;



var formatNumber = d3.format(",.0f"),
    formatCurrency = function(d) { return "$" + formatNumber(d)};

   var buf_indexByName={},
    indexByName = {},
    nameByIndex = {},
    labels = [],
    chords = [];


function log(message) {
   // console.log(message);
}
//Events


