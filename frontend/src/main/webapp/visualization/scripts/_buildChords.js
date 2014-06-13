
function buildChords() {

    var  matrix = [];


    labels=[];
    chords=[];

    for (var i=0; i < columns.length; i++) {
        var l={};
        l.index=i;
        l.label="null";
        l.angle=0;
        labels.push(l);

        var c={}
        c.label="null";
        c.source={};
        c.target={};
        chords.push(c);

    }


    buf_indexByName=indexByName;

    indexByName=[];
    nameByIndex=[];
    n = 0;

    var totalPacAmount=0;

    // Compute a unique index for each package name
    columns.forEach(function(d) {
        d = d.COLUMN_ID;
        if (!(d in indexByName)) {
              nameByIndex[n] = d;
              indexByName[d] = n++;
        }
    });

     columns.forEach(function(d) {
        var source = indexByName[d.COLUMN_ID],
            row = matrix[source];
        if (!row) {
            row = matrix[source] = [];
            for (var i = -1; ++i < n;) row[i] = 0;
        }
        row[indexByName[d.COLUMN_ID]]= Number(d.Amount);
        totalPacAmount+=Number(d.Amount);
    });

  //  console.log("totalPacAmount=" + totalPacAmount)

    chord.matrix(matrix);

    var tempLabels=[];
    var tempChords=[];

    /*
    for (var i=0; i < labels.length; i++) {
        labels[i].label='null';
        chords[i].label='null';
    }

    for (var i=0; i < chord.groups().length; i++) {
        var d={}
        var g=chord.groups()[i];
        var c=chord.chords()[i];
        d.index=i;
        d.angle= (g.startAngle + g.endAngle) / 2;
        d.label = nameByIndex[g.index];
        d.amount= c.source.value;
        d.value= c.source.value;
        var bIndex=buf_indexByName[d.label];
        if (typeof bIndex != 'undefined') {  //Country already exists so re-purpose node.
            labels[bIndex].angle= d.angle;
            labels[bIndex].label= d.label;
            labels[bIndex].index= i;
            labels[bIndex].Amount= Number(d.Amount);

            chords[bIndex].index= i;
            chords[bIndex].label= d.label;
            chords[bIndex].source= c.source;
            chords[bIndex].target= c.target;
            chords[bIndex].Amount = Number(d.Amount);

        }
        else { //Country doesnt currently exist so save for later
            tempLabels.push(d);
            tempChords.push(c);
        }
    }

    //Now use up unused indexes
    for (var i=0; i < labels.length; i++) {
        if (labels[i].label=="null") {
            var o=tempLabels.pop();
            labels[i].index=indexByName[o.label];
            labels[i].label= o.label;
            labels[i].angle= o.angle;
            labels[i].Amount= Number(o.Amount);

            var c=tempChords.pop();
            chords[i].label= o.label;
            chords[i].index= i;
            chords[i].source= c.source;
            chords[i].target= c.target;
            chords[i].Amount= Number(c.Amount);

        }
    }
    */

    chords=chord.chords();

    var i=0;
    chords.forEach(function (d) {
        d.label=nameByIndex[i];
        d.angle=(d.source.startAngle + d.source.endAngle) / 2
        var o={};
        o.startAngle= d.source.startAngle;
        o.endAngle= d.source.endAngle;
        o.index= d.source.index;
        o.value= d.source.value;
        o.currentAngle= d.source.startAngle;
        o.currentLinkAngle= d.source.startAngle;
        o.Amount= d.source.value;
        o.source= d.source;
        o.relatedLinks=[];
        chordsById[d.label]= o;
        i++;
    });


    function getFirstIndex(index,indexes) {
        for (var i=0; i < chordCount; i++) {
            var found=false;
            for (var y=index; y < indexes.length; y++) {
                if (i==indexes[y]) {
                    found=true;
                }
            }
            if (found==false) {
                return i;
                //  break;
            }
        }
        //      console.log("no available indexes");
    }

    function getLabelIndex(name) {
        for (var i=0; i < chordCount; i++) {
            if (buffer[i].label==name) {
                return i;
                //   break;
            }
        }
        return -1;
    }

    log("buildChords()");
}

