import * as React from "react";

interface Password {
    onChange: Function;
    value: string;
}
export const Password = (props: Password) => (
    <input
        className="block bg-neutral-900 h-40px px-10px w-full rounded appearance-none"
        type='password'
        value={props.value}
        onChange={(event) => props.onChange(event.target.value)}
        />
);