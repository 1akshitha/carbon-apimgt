function tierChanged(element) {
    var index = element.selectedIndex;
    var selectedDesc = $("#tierDescriptions").val().split(",")[index];
    $("#tierHelpStr em").text(selectedDesc);
}
$(function () {

    $(".navigation ul li.active").removeClass('active');
    var prev = $(".navigation ul li:first")
    $(".green").insertBefore(prev).css('top','0px').addClass('active');

    var bearerToken = "Bearer 200d5b62-7837-3dd9-b4a8-896cbcd755ab";
    var client = new SwaggerClient({
        url: 'https://apis.wso2.com/api/am/store/v0.10/swagger.json',
        success: function () {
            var applicationId = $("#applicationId").val();
            client.clientAuthorizations.add("apiKey", new SwaggerClient.ApiKeyAuthorization("Authorization", bearerToken, "header"));
            client["Application (individual)"].get_applications_applicationId({"applicationId": applicationId},
                function (jsonData) {

                    var application = jsonData.obj;
                    client["Tier Collection"].get_tiers_tierLevel({"tierLevel": "application"},
                        function (jsonData) {
                            var tierList = jsonData.obj.list;
                            var context = {};
                            context.appTiers = tierList;
                            context.application = application;

                            $.get('/store/public/components/root/base/templates/application/editAppContentTemplate.hbs', function (templateData) {
                                var template = Handlebars.compile(templateData);
                                // Inject data into template
                                var theCompiledHtml = template(context);

                                // Add the compiled html to the page
                                $('#editAppContent').html(theCompiledHtml)
                            }, 'html');


                        },
                        function (error) {
                            console.log('failed with the following: ' + error.statusText);
                        });

                },
                function (error) {
                    console.log('failed with the following: ' + error.statusText);
                });


        }
    });


    $("#appEditForm").validate({
        submitHandler: function (form) {
            updateApplication();
        }
    });

    var updateApplication = function () {
        var application = {};
        var applicationId = $("#applicationId").val();
        application.name = $("#application-name").val();
        application.throttlingTier = $("#appTier").val();
        application.description = $("#description").val();


        client["Application (individual)"].put_applications_applicationId({
                "applicationId":applicationId,
                "body": application,
                "Content-Type": "application/json"
            },
            function (jsonData) {
                window.location = "/store/application/" + jsonData.obj.applicationId;
            },
            function (error) {
                console.log('failed with the following: ' + error.statusText);
            }
        );


    };
})
;



































