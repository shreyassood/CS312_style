// from https://gist.github.com/lou/571b7c0e7797860d6c555a9fdc0496f9#gistcomment-3249288

import { Overlay, Popover } from 'react-bootstrap';
import React, {useEffect, useRef, useState} from "react";

function PopoverStickOnHover({ delay, leaveDelay, onMouseEnter, children, component, placement }) {
    const [showPopover, setShowPopover] = useState(false);
    const childNode = useRef(null);
    let setTimeoutConst = null;
    let setTimeOutLeaveConst = null;

    useEffect(() => {
        return () => {
            if (setTimeoutConst) {
                clearTimeout(setTimeoutConst);
            }
        };
    });

    const handleMouseEnter = () => {
        clearTimeout(setTimeOutLeaveConst);
        setTimeoutConst = setTimeout(() => {
            setShowPopover(true);
            onMouseEnter();
        }, delay);
    };

    const handleMouseLeave = () => {
        clearTimeout(setTimeoutConst);
        setTimeOutLeaveConst = setTimeout(() => {
            setShowPopover(false);
        }, leaveDelay)
    };

    const displayChild = React.Children.map(children, child =>
        React.cloneElement(child, {
            onMouseEnter: handleMouseEnter,
            onMouseLeave: handleMouseLeave,
            ref: node => {
                childNode.current = node;
                const { ref } = child;
                if (typeof ref === 'function') {
                    ref(node);
                }
            }
        })
    )[0];

    return (
        <>
            {displayChild}
            <Overlay
                show={showPopover}
                placement={placement}
                target={childNode}
                shouldUpdatePosition
                flip
            >
                <Popover
                    onMouseEnter={() => {
                        clearTimeout(setTimeOutLeaveConst);
                        setShowPopover(true);
                    }}
                    onMouseLeave={handleMouseLeave}
                    id="popover"
                >
                    {component}
                </Popover>
            </Overlay>
        </>
    );
}

PopoverStickOnHover.defaultProps = {
    delay: 0,
    leaveDelay: 0,
    onMouseEnter: () => {}
};

export default PopoverStickOnHover;