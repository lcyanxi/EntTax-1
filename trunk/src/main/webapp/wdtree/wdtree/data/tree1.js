function createNode(){
  var root = {
    "id" : "0",
    "text" : "菜单",
    "value" : "0",
    "showcheck" : true,
    complete : true,
    "isexpand" : true,
    "checkstate" : 1,
    "hasChildren" : true
  };
  var arr = [];
  for(var i= 1;i<2; i++){
    var subarr = [];
    for(var j=1;j<2;j++){
      var value = "node-aaa" + i + "-" + j; 
      subarr.push( {
         "id" : value,
         "text" : value,
         "value" : value,
         "showcheck" : true,
         complete : true,
         "isexpand" : true,
         "checkstate" : 0,
         "hasChildren" : false
      });
    }
    arr.push( {
      "id" : "node555-" + i,
      "text" : "node-" + i,
      "value" : "node-" + i,
      "showcheck" : true,
      complete : true,
      "isexpand" : true,
      "checkstate" : 0,
      "hasChildren" : true,
      "ChildNodes" : subarr
    });
  }
  root["ChildNodes"] = arr;
  //alert(JSON.stringify(root));
  //o.data = JSON.parse(tmp);
  //alert(root.toJSONString());
  return root; 
}

treedata = createNode();
//treedata = [createNode()];
