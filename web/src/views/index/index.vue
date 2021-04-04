<template>
    <div>
        <div id="myChart" :style="{ width: '300px', height: '300px' }"></div>
    </div>
</template>

<script>
export default {
  name: 'index',
  mounted() {
    //this.$root => app
    let myChart = this.$root.echarts.init(
      document.getElementById("myChart")
    );
    // 绘制图表
    myChart.setOption({
      title: { text: "总用户量" },
      tooltip: {},
      xAxis: {
        data: ["12-3", "12-4", "12-5", "12-6", "12-7", "12-8"],
      },
      yAxis: {},
      series: [
        {
          name: "用户量",
          type: "line",
          data: [5, 20, 36, 10, 10, 20],
        },
      ],
      backgroundColor: 'rgba(255,255,255,0.8)',
    });
  },
}
</script>

