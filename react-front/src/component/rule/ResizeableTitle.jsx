import React from 'react'
import { Resizable } from 'react-resizable'

export default function ResizeableTitle (props) {
  const { onResize, width, className, ...restProps } = props;

  if (!width) {
    return <th {...restProps} />;
  }

  return (
    <Resizable
      width={width}
      height={0}
      className={className}
      onResize={onResize}
      draggableOpts={{ enableUserSelectHack: false }}
    >
      <th {...restProps} />
    </Resizable>
  );
}
