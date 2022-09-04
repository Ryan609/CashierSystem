var xhr = new XMLHttpRequest()
xhr.open('get', '/product/list.json')
xhr.onload = function () {
    console.log(this.status)
    console.log(this.responseText)

    var ret = JSON.parse(this.responseText)

    if (ret.redirectUrl) {
        location.assign(ret.redirectUrl)
    }

    var productList = ret.data;
    var oTbody = document.querySelector('tbody')
    for (var product of productList) {
        var html = "<tr>" +
            `<td>${product.name}</td>` +
            `<td>${product.introduce}</td>` +
            `<td>${product.stock}</td>` +
            `<td>${product.unit}</td>` +
            `<td>${product.price}</td>` +
            `<td>${product.discount}</td>` +
            "</tr>";

        oTbody.innerHTML += html
    }
}

xhr.send()