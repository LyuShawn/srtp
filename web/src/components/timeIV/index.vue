<template>
    <div class="detail">
            <div class="close" @click="closeDetail"><i class="el-icon-close" style="transform:scale(3);"></i></div>
            <div id="IVChart" :style="{ width: '100%', height: '80%' }" style="margin-top:50px;" ></div>
    </div>
</template>
<style>
.detail{
    background:#fff;
    width:70%;
    height:70%;
    position:fixed;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    border-radius:10px;
     box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}
.close{
    float:right;
    
    margin:20px;
    transition:transform 0.2s ease-in-out;
}
.close:hover{
     transform:rotate(180deg);
}
</style>
<script>
export default{
    name: 'detailIV',
    methods:{
        closeDetail(){
            this.$emit('isClosed', false);
        },
        initEChart(){
            var _this=this;
                //this.$root => app
            let myChart = this.$root.echarts.init(
                document.getElementById("IVChart")
            );
            myChart.on('click',function(params){
                _this.showIV = true;
            });
            // 绘制图表
            myChart.setOption({
                title: { text: "   I-V曲线" },
                tooltip: {},
                legend: {
                show:false,
                },
                xAxis: {
                boundaryGap: false,
                data: ["12-3", "12-4", "12-5", "12-6", "12-7", "12-8"],
                },
                yAxis: {
                boundaryGap: false,
                
                data: [" ","正常","开路","短路","阴影",""],
                },
                series: [
                {
                    name: "板1",
                    type: "line",
                    data: [0, 1, 2, 3, "阴影", 0],
                    smooth: true,
                },
                ],
                backgroundColor: 'rgba(255,255,255,0.8)',
            });
         },
    },
    mounted(){
        this.initEChart();
    }
}
</script>