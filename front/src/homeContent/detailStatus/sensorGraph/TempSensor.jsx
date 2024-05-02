import React from 'react';
import ReactApexChart from 'react-apexcharts';
import './tempSensor.css';

function TempSensor({ plantDetail, kitTemp }) {
    const [series, setSeries] = React.useState([kitTemp *2.5]);

    const plantAvgTemp = plantDetail?.plantTemp?.split('~') || '';
    const plantLeastTemp = plantDetail?.plantWinterTemp || '';

    React.useEffect(() => {
        if (kitTemp === '') {
            setSeries(['notYet'])
        } else {
            setSeries([kitTemp * 2.5]);
        }
    }, [kitTemp]);

    function getTemperatureStatus(plantAvgTemp, plantLeastTemp, series) {
        const measuredTemp = series / 2.5;  

        if (measuredTemp >= plantAvgTemp[0] && measuredTemp <= plantAvgTemp[1]) {
            return 'Optimal';
        }

        if (measuredTemp < plantAvgTemp[0]) {
            return (
                <div style={{ textAlign: 'center' }}>
                    {`${(plantAvgTemp[0] - measuredTemp).toFixed(2)}°C Below`}
                </div>
            )
        }

        if (measuredTemp > plantAvgTemp[1]) {
            return (
                <div style={{ textAlign: 'center' }}>
                    {`${(measuredTemp - plantAvgTemp[1]).toFixed(2)}°C Above`} 
                </div>
            )
        }

        return 'No Data'; 
    }

    const options = {
        chart: {
            type: 'radialBar',
            offsetY: -20,
            parentHeightOffset: 0,
        },
        plotOptions: {
            radialBar: {
                startAngle: -90,
                endAngle: 90,
                min: 0,
                max: 40,
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
                            return val/2.5 + '°C';
                        }
                    }
                },
            }
        },
        fill: {
            type: 'gradient',
            gradient: {
                shade: 'light',
                type: 'horizontal',
                shadeIntensity: 0.5,
                gradientToColors: ['#FF8888'], 
                inverseColors: false,
                opacityFrom: 1,
                opacityTo: 1,
                stops: [0, 40]
            }
        },
        series: series,
        stroke: {
            lineCap: 'round'
        },
        labels: ['Temperature'],
    }
    
    return (
        <div style={{ position: 'relative' }}>
            <ReactApexChart id='chart' options={options} series={series} type="radialBar" height='100%' paddingBottom="10px" />
            <div style={{
                position: 'absolute',
                left: '50%',
                transform: 'translateX(-50%)',
                fontSize: '16px',
                top: '70%',
                bottom: '20%'
            }}>
                {getTemperatureStatus(plantAvgTemp, plantLeastTemp, series)}
            </div>

        </div>
    )


}

export default TempSensor;