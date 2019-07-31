(function(document, screen) {
    var width = Math.min(document.documentElement.offsetWidth, screen.width)
    width = width > 375 ? 375 : width;
    var height = Math.min(document.documentElement.clientHeight, screen.height) || 550;
    var wl = Math.min(width / 375, height / 550);
    var ft = wl * 37.5 + 'px';
    document.getElementsByTagName("html")[0].style.fontSize = ft;

    var i = 0;
    if (!i) {
        var p = window.navigator.userAgent,
            q = (!!p.match(/android/gi), !!p.match(/iphone/gi)),
            r = q && !!p.match(/OS 9_3/),
            s = window.devicePixelRatio;
        i = q && !r ? s >= 3 && (!i || i >= 3) ? 3 : s >= 2 && (!i || i >= 2) ? 2 : 1 : 1
    }
    document.documentElement.setAttribute("data-dpr", i);
}(document, screen));