$.fn.extend({
    refreshCode:function (options) {
        if(!options.lazyUrl){
            alert("您必须指定：lazyUrl属性")
            return;
        }
        this.attr("src",options.lazyUrl+"?ts="+new Date().getMilliseconds())
        $(this).click(function () {
            $(this).attr("src",options.lazyUrl+"?ts="+new Date().getMilliseconds())
        })
    },
    actions:function (options) {
        options=$.extend({
            vectorName:"inputVector"
        },options)
        //获取当前表单对象
        var form=$(this)
        var timemap=form.data("datamap")
        if(!timemap){
            timemap={}
        }
        for(var i=0;i<form.find("input").length;i++){
            $(form.find("input")[i]).data("id",i)
        }
        form.find("input").keydown(function(){
            if($(this).val().trim().length==0){
                $(this).data("starttime",event.timeStamp)
            }
            $(this).data("length",$(this).val().trim().length)
        })
        form.find("input").keyup(function(){
            if(!$(this).data("starttime")){
                $(this).data("starttime",event.timeStamp)
            }
            var duration=event.timeStamp - $(this).data("starttime")
            timemap[$(this).data("id")]=duration
            form.data("datamap",timemap)
            //计算特征向量
            var keys=[]
            for(var i in timemap){
                keys.push(i)
            }
            keys.sort()
            var vectors=[]
            keys.forEach(function (key,i) {
                vectors.push(timemap[key])
            })
            form.data(options.vectorName,vectors)
        })
    }
})