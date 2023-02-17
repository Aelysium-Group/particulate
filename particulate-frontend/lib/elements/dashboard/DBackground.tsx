import { useEffect, useState } from 'react';
import { useHookOntoScreen } from 'react-ui-breakpoints';

type DBackground = {
    active: boolean;
    children?: JSX.Element | JSX.Element[];
}
export const DBackground = (props: DBackground) => {
    const auto = useHookOntoScreen();
    const [ size ] = useState(document.body.clientWidth > 800 ? 100 : 50);
    const [ cols, setCols ] = useState(0);
    const [ rows, setRows ] = useState(0);
    const [ toggled, setToggle ] = useState(false);

    useEffect(()=>{
        setCols(Math.floor(window.innerWidth / size));
        setRows(Math.floor(window.innerHeight / size));
    },[auto]);

    const render = () => {
        let tiles: JSX.Element[] = [];
        for (let i = 0; i < (cols * rows); i++)
            tiles.push(<div key={i} className={`w-124px aspect-square relative before:duration-[0.5s] before:absolute before:inset-1px before:bg-template-gray`}></div>);

        return (
            <div
                className={`absolute w-screen h-screen inset-0 opacity-90 bg-gradient-to-t from-emerald-900 to-blue-900 z-0`}
                style={{width: (124 * cols) + "px", height: (124 * rows) + "px"}}
                draggable={false}
                >
                <div
                    className={`w-screen h-screen duration-[0.5s] ${ props.active ? "bg-transparent" : "bg-template-gray/90"}`}
                    draggable={false}
                    >
                    <div
                        className={`grid grid-flow-dense w-screen h-screen`}
                        draggable={false}
                        style={{
                            gridTemplateColumns: `repeat(${cols},1fr)`,
                            gridTemplateRows: `repeat(${rows},1fr)`,
                            background: "radial-gradient(circle, rgba(32,33,36,0) 0%, rgba(32,33,36,1) 100%)"
                        }}
                        >
                        {tiles}
                    </div>
                </div>
            </div>
        );
    }

    return render();
}