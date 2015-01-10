function activateArticle(name) {
    var ajaxCallBack = {
        success: onSuccess,
        error: onError
    }

    jsRoutes.controllers.Application.activateArticle(name).ajax(ajaxCallBack);
    var onSuccess = function (data) {
        alert(data);
    }

    var onError = function (error) {
        alert(error);
    }
};

