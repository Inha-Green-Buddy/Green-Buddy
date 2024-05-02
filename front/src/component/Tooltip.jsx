import React from 'react';
import { Button, Tooltip, OverlayTrigger } from 'react-bootstrap';
import './toolTip.css';

const ToolTip = () => {
    const renderTooltip = (props) => (
        <Tooltip id="button-tooltip" {...props} className="custom-tooltip">
            <span>
                Angel Wing Begonia<br />
                Areca Palm<br />
                Broadleaf Lady Palm<br />
                Fiddle Leaf Fig<br />
                Gardenia Jasminoides<br />
                Geranium<br />
                Goldcrest Wilma<br />
                Golden Pothos<br />
                Heartleaf Philodendron<br />
                Lucky Bamboo<br />
                Money Tree<br />
                Monstera Deliciosa<br />
                Rubber Plant<br />
                Snake Plant<br />
                Cycad
            </span>
        </Tooltip>
    );

    return (
        <OverlayTrigger
            placement="bottom"
            overlay={renderTooltip}
        >
            <Button variant="primary" style={{ backgroundColor: '#dfd880', border: 'none', borderRadius: '50px', color: 'white', marginTop: '10px' }}>Registerable Plant</Button>
        </OverlayTrigger>
    )
};

export default ToolTip;
