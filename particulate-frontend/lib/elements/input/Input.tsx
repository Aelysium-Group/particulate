interface TextInput {
    onChange: Function;
    value: string;
    placeholder?: string;
}
export const TextInput = (props: TextInput) => (
    <input
        className="block bg-neutral-300 h-47px w-full my-10px px-10px py-4px pt-3px border-none rounded-xl appearance-none shadow-inset-md"
        type='text'
        value={props.value}
        onChange={(event) => props.onChange(event.target.value)}
        placeholder={props.placeholder ?? ''}
        />
);