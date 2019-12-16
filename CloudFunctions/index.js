const functions = require('firebase-functions');
const admin = require('firebase-admin');
const config = functions.config();
admin.initializeApp(config.firebase);

exports.newGroupCreated = functions.database.ref("/groups/{groupID}/members").onCreate((data, context) => {  
      var groupID; 
      var members = {};
      groupID = context.params.groupID;
      members = data.val();
      for (var key in members) {
	    var group = {};
	    group[groupID] = 0;
	    admin.database().ref('/users/' + key).child('groups').update(group);
      }
      return 0; 
});

