function draw_ind(div) {

    // get the data
    d3.csv("ind_data.csv", function(links) {

        var nodes = {};

        // Compute the distinct nodes from the links.
        links.forEach(function(link) {
            link.source = nodes[link.source] ||
                (nodes[link.source] = {name: link.source});
            link.target = nodes[link.target] ||
                (nodes[link.target] = {name: link.target});
            link.value = +link.value;
        });

        var width = 960,
            height = 500;

        var force = d3.layout.force()
            .nodes(d3.values(nodes))
            .links(links)
            .size([width, height])
            .linkDistance(60)
            .charge(-300)
            .on("tick", tick)
            .start();

        var svg = d3.select(div).append("svg")
            .attr("width", width)
            .attr("height", height);

        // build the arrow.
        svg.append("svg:defs").selectAll("marker")
            .data(["end"])
            .enter().append("svg:marker")
            .attr("id", String)
            .attr("viewBox", "0 -5 10 10")
            .attr("refX", 15)
            .attr("refY", -1.5)
            .attr("markerWidth", 6)
            .attr("markerHeight", 6)
            .attr("orient", "auto")
            .append("svg:path")
            .attr("d", "M0,-5L10,0L0,5");

        // add the links and the arrows
        var path = svg.append("svg:g").selectAll("path")
            .data(force.links())
            .enter().append("svg:path")
            .attr("class", "link")
            .attr("marker-end", "url(#end)");

        // define the nodes
        var node = svg.selectAll(".node")
            .data(force.nodes())
            .enter().append("g")
            .attr("class", "node")
            .call(force.drag);

        // add the nodes
        node.append("circle")
            .attr("r", 5);

        // add the text
        node.append("text")
            .attr("x", 12)
            .attr("dy", ".35em")
            .text(function(d) { return d.name; });

    // add the curvy lines
        function tick() {
            path.attr("d", function(d) {
                var dx = d.target.x - d.source.x,
                    dy = d.target.y - d.source.y,
                    dr = Math.sqrt(dx * dx + dy * dy);
                return "M" +
                    d.source.x + "," +
                    d.source.y + "A" +
                    dr + "," + dr + " 0 0,1 " +
                    d.target.x + "," +
                    d.target.y;
            });

            node
                .attr("transform", function(d) {
                    return "translate(" + d.x + "," + d.y + ")"; });
        }

    });
     
}



function draw_ucc(div) {

    var w = 1280,
        h = 800,
        r = 720,
        x = d3.scale.linear().range([0, r]),
        y = d3.scale.linear().range([0, r]),
        node,
        root;

    var pack = d3.layout.pack()
        .size([r, r])
        .value(function(d) { return d.size; })

    var vis = d3.select(div).insert("svg:svg", "h2")
        .attr("width", w)
        .attr("height", h)
        .append("svg:g")
        .attr("transform", "translate(" + (w - r) / 2 + "," + (h - r) / 2 + ")");

    d3.json("ucc_data.json", function(data) {
        node = root = data;

        var nodes = pack.nodes(root);

        vis.selectAll("circle")
            .data(nodes)
            .enter().append("svg:circle")
            .attr("class", function(d) { return d.children ? "parent" : "child"; })
            .attr("cx", function(d) { return d.x; })
            .attr("cy", function(d) { return d.y; })
            .attr("r", function(d) { return d.r; })
            .on("click", function(d) { return zoom(node == d ? root : d); });

        vis.selectAll("text")
            .data(nodes)
            .enter().append("svg:text")
            .attr("class", function(d) { return d.children ? "parent" : "child"; })
            .attr("x", function(d) { return d.x; })
            .attr("y", function(d) { return d.y; })
            .attr("dy", ".35em")
            .attr("text-anchor", "middle")
            .style("opacity", function(d) { return d.r > 20 ? 1 : 0; })
            .text(function(d) { return d.name; });

        d3.select(window).on("click", function() { zoom(root); });
    });

    function zoom(d, i) {
        var k = r / d.r / 2;
        x.domain([d.x - d.r, d.x + d.r]);
        y.domain([d.y - d.r, d.y + d.r]);

        var t = vis.transition()
            .duration(d3.event.altKey ? 7500 : 750);

        t.selectAll("circle")
            .attr("cx", function(d) { return x(d.x); })
            .attr("cy", function(d) { return y(d.y); })
            .attr("r", function(d) { return k * d.r; });

        t.selectAll("text")
            .attr("x", function(d) { return x(d.x); })
            .attr("y", function(d) { return y(d.y); })
            .style("opacity", function(d) { return k * d.r > 20 ? 1 : 0; });

        node = d;
        d3.event.stopPropagation();
    }

}