'use strict';

app.constant('CONSTANTS', {
	responseStatusCodes : {
		SUCCESS : 200,
		NOT_FOUND : 404,
		BAD_REQUEST : 400,
		UNAUTHORIZED : 401
	},
	appConfig : {
		showLoader : 'show_loader',
		opGrpNames : [ "Financial Services", "Health & Public Services",
				"Products", "Communications Media & Technology", "Resources",
				"Cross Industry" ]
	},
	editApplication : {
		pageHeading : "Edit Application Details",
		submitBtnText : "Save"
	},
	createApplication : {
		pageHeading : "Add Application Details",
		submitBtnText : "Add Application"
	},
	errorSuccessMessages : {
		DEL_SUCCESS_MSG : "Application deleted successfully!!",
		DEL_ERROR_MSG : "Error while deleting application"
	}
});
