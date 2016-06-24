$('.menu .item').tab();

$.ajax({
    type: 'GET',
    url: '/app/subscriptions',
    dataType: 'json',
    success: function(subscriptions){
        jQuery.each(subscriptions, function(i, subscription) {
            var subscriptionTable = '';
            jQuery.each(subscription.users, function(i, user) {
                if (subscriptionTable != '') subscriptionTable += '<br>';
                subscriptionTable += user.firstname + " " + user.lastname + "&nbsp;<a href='/login/openid?openid_identifier=" + user.openId + "'> Login</a>";
            });
            $('#subscriptionTable').append('<tr><td>' + subscription.id + '</td><td>' + subscription.companyName + '</td><td>' + subscription.edition + '</td><td>' + subscriptionTable + '</td></tr>');
        });
    }
});