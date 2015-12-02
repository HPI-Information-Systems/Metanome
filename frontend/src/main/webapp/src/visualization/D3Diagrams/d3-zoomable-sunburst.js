var d3_zoomable_sunburst = function (input_file_path, canvas_width, canvas_height, callback) {
  d3.json(input_file_path, function (error, data) {

    if (error) return console.error(error);

    var json_data = data;
    d3_zoomable_sunburst_private(json_data, canvas_width, canvas_height);

    if (!(callback === undefined)) {
      callback();
    }
  });
};

// Is called by the d3_zoomable_sunburst function after loading the JSON data from file
var d3_zoomable_sunburst_private = function (json_data, canvas_width, canvas_height) {
  //currently selected node after clicking (for breadcrumb)
  var current = null;

  var root = json_data;

  var scale_width = 150,
    width = canvas_width - scale_width - 20,
    height = canvas_height * 0.6,
    radius = Math.min(width, height) / 2;

  // Breadcrumb dimensions: width, height, spacing, width of tip/tail.
  var b = {
    w: 125, h: 30, s: 3, t: 10
  };

  var x = d3.scale.linear()
    .range([0, 2 * Math.PI]);

  var y = d3.scale.sqrt()
    .range([0, radius]);

  var color = d3.scale.category20c();


  var vis = d3.select("body").append("svg:svg")
    .attr("width", width)
    .attr("height", height)
    .append("svg:g")
    .attr("id", "container")
    .attr("transform", "translate(" + (width / 2) + "," + (height / 2) + ")");

  var partition = d3.layout.partition()
    .value(function (d) {
      return 1;
    });

  var arc = d3.svg.arc()
    .startAngle(function (d) {
      return Math.max(0, Math.min(2 * Math.PI, x(d.x)));
    })
    .endAngle(function (d) {
      return Math.max(0, Math.min(2 * Math.PI, x(d.x + d.dx)));
    })
    .innerRadius(function (d) {
      return Math.max(0, y(d.y));
    })
    .outerRadius(function (d) {
      return Math.max(0, y(d.y + d.dy));
    });

  var tooltip = d3.select("body")
    .append("div")
    .attr("class", "tooltip")
    .style("position", "absolute")
    .style("z-index", "10")
    .style("opacity", 0);

  // Basic setup of page elements.
  initializeBreadcrumbTrail();

  // Creation of sunburst

  d3.select("#container").on("mouseleave", mouseleave);

  var path = vis.data([json_data]).selectAll("path")
    .data(partition.nodes(root))
    .enter().append("path")
    .attr("d", arc)
    .style("fill", function (d) {
      return color(d.name);
    })
    .on("click", click)
    .on("mouseover", mouseover)
    .on("mousemove", function (d) {
      return tooltip
        .style("top", (d3.event.pageY - 10) + "px")
        .style("left", (d3.event.pageX + 10) + "px")
        .style("visibility", "");
    })
    .on("mouseout", function () {
      return tooltip.style("opacity", 0);
    });


  function mouseover(d) {
    var keyError = d.keyError;
    var keyErrorString = "";
    if (keyError != undefined) {
      d3.select("#separatingLabel")
        .style("visibility", "");
      if (keyError == 0) {
        keyErrorString = "This is a valid FD";
      }
      else {
        keyErrorString = keyError + "/" + root.tableSize + " duplicates preventing FD";
      }
    }
    else {
      d3.select("#separatingLabel")
        .style("visibility", "hidden");
    }

    var sequenceArray = getAncestors(d);
    updateBreadcrumbs(sequenceArray, keyErrorString);
    tooltip.html(d.name);
    return tooltip.transition()
      .duration(50)
      .style("opacity", 0.9)
      .style("background", color(d.name));
  }

  function mouseleave() {
    //if current is null, hide the breadcrumb trail (nothing selected yet)
    if (current == null) {
      d3.select("#trail")
        .style("visibility", "hidden");
    }
    //else, keep the currently selected node
    else {
      var keyError = current.keyError;
      var keyErrorString = "";
      if (keyError != undefined) {
        if (keyError == 0) {
          keyErrorString = "This is a valid FD";
        }
        else {
          keyErrorString = keyError + "/" + root.tableSize + " duplicates preventing FD";
        }
      }
      else {
        d3.select("#separatingLabel")
          .style("visibility", "hidden");
      }
      var sequenceArray = getAncestors(current);
      updateBreadcrumbs(sequenceArray, keyErrorString);
    }
    //also, hide the tooltip
    d3.select("div.tooltip")
      .style("visibility", "hidden");
  }

  function click(d) {
    current = d;
    if (current.keyError == undefined) {
      d3.select("#separatingLabel")
        .style("visibility", "hidden");
    }
    path.transition()
      .duration(300)
      .attrTween("d", arcTween(d))
      .style("opacity", function (d2, i2) {
        return ((d2.x >= d.x + d.dx) || (d2.x + d2.dx <= d.x) ) ?
          //is this data outside of the domain?
          0 : //yes - zero opacity (transparent)
          1; //no - full opacity (visible)
      });
  }

  // Given a node in a partition layout, return an array of all of its ancestor
  // nodes, highest first, but excluding the root.
  function getAncestors(node) {
    var path = [];
    var current = node;
    while (current.parent) {
      path.unshift(current);
      current = current.parent;
    }
    return path;
  }

  function initializeBreadcrumbTrail() {
    // Add the svg area.
    var trail = d3.select("#sequence").append("svg:svg")
      .attr("width", canvas_width)
      .attr("height", 100)
      .attr("id", "trail");

    // Add the label at the end, for the keyError.
    trail.append("svg:text")
      .attr("id", "endlabel")
      .style("fill", "#000");

    //label to separate dependant and determinant
    trail.append("svg:text")
      .attr("id", "separatingLabel")
      .style("fill", "black")
      .style("font-size", "xx-large")
      .style("color", "red")
      .style("font-weight", "bold");
  }

  // Generate a string that describes the points of a breadcrumb polygon.
  function breadcrumbPoints(d, i) {
    var points = [];
    points.push("0,0");
    //leftmost breadcrumb, make it a rectangle
    if (i == 0) {
      points.push(b.w + ",0");
      points.push(b.w + "," + b.h);
      points.push("0," + b.h);
    }
    else {
      points.push(b.w + ",0");
      points.push(b.w + b.t + "," + (b.h / 2));
      points.push(b.w + "," + b.h);
      points.push("0," + b.h);
      //second breadcrumb needs a flat left side
      if (i > 1) {
        points.push(b.t + "," + (b.h / 2));
      }
    }
    return points.join(" ");
  }

  // Update the breadcrumb trail to show the current sequence and keyError.
  function updateBreadcrumbs(nodeArray, keyErrorString) {


    // Data join; key function combines name and depth (= position in sequence).
    var g = d3.select("#trail")
      .selectAll("g")
      .data(nodeArray, function (d) {
        return d.name + d.depth;
      });

    // Add breadcrumb and label for entering nodes.
    var entering = g.enter().append("svg:g");

    entering.append("svg:polygon")
      .attr("points", breadcrumbPoints)
      .style("fill", function (d) {
        return color(d.name)
      });

    entering.append("svg:text")
      .attr("x", (b.w + b.t) / 2)
      .attr("y", b.h / 2)
      .attr("dy", "0.35em")
      .attr("text-anchor", "middle")
      .text(function (d) {
        return d.name;
      });

    //first, creat the end label text (need its width for computation)
    d3.select("#trail").select("#endlabel")
      .text(keyErrorString)
      .attr("dy", "0.35em")
      .attr("text-anchor", "start")

    // calculate how many nodes can fit into the horizontal space
    var endLabelLength = d3.select("#trail").select("#endlabel").node().getComputedTextLength();
    var maxHorizontalWidth = canvas_width - 20 - 40 - endLabelLength
    var maxHorizontalNumber = Math.floor(maxHorizontalWidth / (b.w + b.s));


    // Set position for entering and updating nodes.
    g.attr("transform", function (d, i) {
      if (i > 0) {
        //if the width of i+1 (= the right side of the trail) exceeds the canvas width, translate it one level down
        if (i > (maxHorizontalNumber - 1)) {
          //move the end label to the end of the second row
          d3.select("#trail").select("#endlabel")
            .attr("x", ((nodeArray.length - (maxHorizontalNumber - 1)) + 0.45) * (b.w + b.s))
            .attr("y", b.h + 5 + b.h / 2);
          return "translate(" + ((i - (maxHorizontalNumber - 1)) * (b.w + b.s) + 40) + ", " + (b.h + 5) + ")";
        }
        else {
          //move the end label to the end of the first row
          d3.select("#trail").select("#endlabel")
            .attr("x", (nodeArray.length + 0.45) * (b.w + b.s))
            .attr("y", b.h / 2);
          return "translate(" + (i * (b.w + b.s) + 40) + ", 0)";
        }
      }
      else {
        return "translate(" + 0 + ", 0)";
      }
    });

    // Remove exiting nodes.
    g.exit().remove();


    //move separating label between dependant and determinant
    d3.select("#trail").select("#separatingLabel")
      .attr("x", b.w + 20)
      .attr("y", b.h / 2)
      .attr("dy", "0.35em")
      .attr("text-anchor", "middle")
      .text("\u2190");

    // Make the breadcrumb trail visible, if it's hidden.
    d3.select("#trail")
      .style("visibility", "");

  }

  // Get the names out of the json data
  var names = [];
  for (var i = 0; i < root.children.length; i++) {
    names.push(root.children[i].name);
  }

  var legend = d3.select("body").append("svg")
    .attr("class", "legend")
    .attr("width", scale_width - 5)
    .attr("height", d3.max([height, names.length * 20]))
    .selectAll("g")
    .data(names)
    .enter().append("g")
    .attr("transform", function (d, i) {
      return "translate(0," + i * 20 + ")";
    });

  legend.append("rect")
    .attr("width", 10)
    .attr("height", 10)
    .attr("y", 4)
    .style("fill", function (d) {
      return color(d)
    });

  legend.append("text")
    .attr("x", 24)
    .attr("y", 9)
    .attr("dy", ".35em")
    .text(function (d) {
      return d
    });

  // Creation of sunburst finished

  // Interpolate the scales!
  function arcTween(d) {
    var xd = d3.interpolate(x.domain(), [d.x, d.x + d.dx]),
      yd = d3.interpolate(y.domain(), [d.y, 1]),
      yr = d3.interpolate(y.range(), [d.y ? 20 : 0, radius]);
    return function (d, i) {
      return i
        ? function (t) {
        return arc(d);
      }
        : function (t) {
        x.domain(xd(t));
        y.domain(yd(t)).range(yr(t));
        return arc(d);
      };
    };
  }

};
