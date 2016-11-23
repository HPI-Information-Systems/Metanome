var d3_sequences_sunburst = function (input_file_path, canvas_width, canvas_height, callback) {
  d3.json(input_file_path, function (error, data) {

    if (error) return console.error(error);

    var json_data = data;
    d3_sequences_sunburst_private(json_data, canvas_width, canvas_height);

    if (!(callback === undefined)) {
      callback();
    }
  });
};

// Is called by the d3_zoomable_sunburst function after loading the JSON data from file
var d3_sequences_sunburst_private = function (json_data, canvas_width, canvas_height) {

  var root = json_data;

  var scale_width = 150,
    width = canvas_width - scale_width,
    height = canvas_height,
    radius = Math.min(width, height) / 2;

  // Breadcrumb dimensions: width, height, spacing, width of tip/tail.
  var b = {
    w: 125, h: 30, s: 3, t: 10
  };

  // Total size of all segments; we set this later, after loading the data.
  var totalSize = 0;

  var color = d3.scale.category20c();

  d3.select("#explanation")
    .style("top", height / 2 - 70 + 'px')
    .style("left", width / 2 - 70 + 'px');

  console.log(Math.sqrt(radius));

  var vis = d3.select("#chart").append("svg:svg")
    .attr("width", width)
    .attr("height", height)
    .append("svg:g")
    .attr("id", "container")
    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

  var partition = d3.layout.partition()
    .size([2 * Math.PI, radius * radius])
    .value(function (d) {
      return d.size;
    });

  var arc = d3.svg.arc()
    .startAngle(function (d) {
      return d.x;
    })
    .endAngle(function (d) {
      return d.x + d.dx;
    })
    .innerRadius(function (d) {
      return Math.sqrt(d.y);
    })
    .outerRadius(function (d) {
      return Math.sqrt(d.y + d.dy);
    });


  createVisualization(json_data);

  // Main function to draw and set up the visualization, once we have the data.
  function createVisualization(json) {

    // Basic setup of page elements.
    initializeBreadcrumbTrail();
    drawLegend();
    d3.select("#togglelegend").on("click", toggleLegend);

    // Bounding circle underneath the sunburst, to make it easier to detect
    // when the mouse leaves the parent g.
    vis.append("svg:circle")
      .attr("r", radius)
      .style("opacity", 0);

    // For efficiency, filter nodes to keep only those large enough to see.
    var nodes = partition.nodes(json)
      .filter(function (d) {
        return (d.dx > 0.005); // 0.005 radians = 0.29 degrees
      });

    var path = vis.data([json]).selectAll("path")
      .data(nodes)
      .enter().append("svg:path")
      .attr("display", function (d) {
        return d.depth ? null : "none";
      })
      .attr("d", arc)
      .attr("fill-rule", "evenodd")
      .style("fill", function (d) {
        return color(d.name);
      })
      .style("opacity", 1)
      .on("mouseover", mouseover);

    // Add the mouseleave handler to the bounding circle.
    d3.select("#container").on("mouseleave", mouseleave);

    // Get total size of the tree = value of root node from partition.
    totalSize = path.node().__data__.value;
  };

  // Fade all but the current sequence, and show it in the breadcrumb trail.
  function mouseover(d) {

    var keyError = (d.keyError).toPrecision(3);
    var percentageString = keyError;


    d3.select("#percentage")
      .text(percentageString);

    d3.select("#explanation")
      .style("visibility", "");

    var sequenceArray = getAncestors(d);
    updateBreadcrumbs(sequenceArray, percentageString);

    // Fade all the segments.
    d3.selectAll("path")
      .style("opacity", 0.3);

    // Then highlight only those that are an ancestor of the current segment.
    vis.selectAll("path")
      .filter(function (node) {
        return (sequenceArray.indexOf(node) >= 0);
      })
      .style("opacity", 1);
  }

  // Restore everything to full opacity when moving off the visualization.
  function mouseleave(d) {

    // Hide the breadcrumb trail
    d3.select("#trail")
      .style("visibility", "hidden");

    // Deactivate all segments during transition.
    d3.selectAll("path").on("mouseover", null);

    // Transition each segment to full opacity and then reactivate it.
    d3.selectAll("path")
      .transition()
      .duration(1000)
      .style("opacity", 1)
      .each("end", function () {
        d3.select(this).on("mouseover", mouseover);
      });

    d3.select("#explanation")
      .style("visibility", "hidden");
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
      .attr("width", width)
      .attr("height", 50)
      .attr("id", "trail");
    // Add the label at the end, for the percentage.
    trail.append("svg:text")
      .attr("id", "endlabel")
      .style("fill", "#000");
  }

  // Generate a string that describes the points of a breadcrumb polygon.
  function breadcrumbPoints(d, i) {
    var points = [];
    points.push("0,0");
    points.push(b.w + ",0");
    points.push(b.w + b.t + "," + (b.h / 2));
    points.push(b.w + "," + b.h);
    points.push("0," + b.h);
    if (i > 0) { // Leftmost breadcrumb; don't include 6th vertex.
      points.push(b.t + "," + (b.h / 2));
    }
    return points.join(" ");
  }

  // Update the breadcrumb trail to show the current sequence and percentage.
  function updateBreadcrumbs(nodeArray, percentageString) {

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

    // Set position for entering and updating nodes.
    g.attr("transform", function (d, i) {
      return "translate(" + i * (b.w + b.s) + ", 0)";
    });

    // Remove exiting nodes.
    g.exit().remove();

    // Now move and update the percentage at the end.
    d3.select("#trail").select("#endlabel")
      .attr("x", (nodeArray.length + 0.5) * (b.w + b.s))
      .attr("y", b.h / 2)
      .attr("dy", "0.35em")
      .attr("text-anchor", "middle")
      .text(percentageString);

    // Make the breadcrumb trail visible, if it's hidden.
    d3.select("#trail")
      .style("visibility", "");

  }

  function drawLegend() {

    // Dimensions of legend item: width, height, spacing, radius of rounded rect.
    var li = {
      w: scale_width, h: 30, s: 3, r: 3
    };

    var names = [];
    names.push(root.name);
    for (var i = 0; i < root.children.length; i++) {
      names.push(root.children[i].name);
    }


    var legend = d3.select("#legend").append("svg:svg")
      .attr("width", li.w)
      .attr("height", names.length * (li.h + li.s));

    var g = legend.selectAll("g")
      .data(names)
      .enter().append("svg:g")
      .attr("transform", function (d, i) {
        return "translate(0," + i * (li.h + li.s) + ")";
      });

    g.append("svg:rect")
      .attr("rx", li.r)
      .attr("ry", li.r)
      .attr("width", li.w)
      .attr("height", li.h)
      .style("fill", function (d) {
        return color(d);
      });

    g.append("svg:text")
      .attr("x", li.w / 2)
      .attr("y", li.h / 2)
      .attr("dy", "0.35em")
      .attr("text-anchor", "middle")
      .text(function (d) {
        return d;
      });
  }

  function toggleLegend() {
    var legend = d3.select("#legend");
    if (legend.style("visibility") == "hidden") {
      legend.style("visibility", "");
    } else {
      legend.style("visibility", "hidden");
    }
  }

};
