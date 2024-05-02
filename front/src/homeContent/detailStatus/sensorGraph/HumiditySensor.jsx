import React from 'react';
import ReactApexChart from 'react-apexcharts';
import './humiditySensor.css';

function HumiditySensor({ plantDetail, kitHumidity }) {
    const [series, setSeries] = React.useState([(kitHumidity - 4095) * 100 / 1095]); // 그대로
    const plantAvgHumidity = plantDetail?.plantHumidity?.split('~') || [];

    React.useEffect(() => {
        if (kitHumidity === '' || kitHumidity === 0) {
            setSeries(['NaN'])
        } else {
            const normalizedHumidity = (kitHumidity - 4095) * 100 / 1095;
            setSeries([normalizedHumidity]);
        }
    }, [kitHumidity]);


    function getHumidityStatus(plantAvgHumidity, series) {
        const measuredHumidity = series * 1.0;  // 변환되어야 함

        if (measuredHumidity >= plantAvgHumidity[0] && measuredHumidity <= plantAvgHumidity[1]) {
            return 'Optimal';
        }

        if (measuredHumidity < plantAvgHumidity[0]) {

            return (
                <div style={{ textAlign: 'center' }}>
                    {`${plantAvgHumidity[0] - measuredHumidity}% Below`}
                </div>
            )
        }


        if (measuredHumidity > plantAvgHumidity[1]) {
            return (
                <div style={{ textAlign: 'center' }}>
                    {`${measuredHumidity - plantAvgHumidity[1]}% Above`}
                </div>
            )
        }

        return 'No Data';
    }

    const options = {
        chart: {
            type: 'radialBar',
            offsetY: -20,
            parentHeightOffset: 0
        },
        plotOptions: {
            radialBar: {
                startAngle: -90,
                endAngle: 90,
                min: 0,
                max: 100,
                track: {
                    background: '#e7e7e7',
                    strokeWidth: '97%',
                    margin: 5,
                    dropShadow: {
                        enabled: true,
                        top: 2,
                        left: 0,
                        blur: 3,
                        opacity: 0.5
                    }
                },
                dataLabels: {
                    name: {
                        show: false
                    },
                    value: {
                        offsetY: 25,
                        fontSize: '22px',
                        formatter: function (val) {
                            return val + '%';
                        }
                    }
                },
            }
        },
        fill: {
            // type: 'gradient',
            gradient: {
                shade: 'light',
                type: 'horizontal',
                shadeIntensity: 0.5,
                gradientToColors: ['#A9CFF1'],
                inverseColors: false,
                opacityFrom: 1,
                opacityTo: 1,
                stops: [0, 100]
            }
        },
        series: series,
        stroke: {
            lineCap: 'round'
        },
        labels: ['Humidity'],
    }

    return (
        <div style={{ position: 'relative' }}>
            <ReactApexChart id='chart' options={options} series={series} type="radialBar" height='100%' paddingBottom="10px" />
            <div style={{
                position: 'absolute',
                left: '50%',  // 가로 중앙에 배치하기 위해 50%로 설정
                transform: 'translateX(-50%)',  // 가로 방향으로 자기 자신의 반만큼 이동
                fontSize: '16px',
                top: '70%',
                bottom: '20%',
                textAlign: 'center'
            }}>
                { getHumidityStatus(plantAvgHumidity, series) }
            </div>

        </div>
    )
}

export default HumiditySensor;