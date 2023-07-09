import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import { App } from './App'
import { MessageDispatcher } from './websocket-provider/providers/MessageDispatcher'
import { Control, ParseableControlObject } from './elements/generic/controls/Control'

MessageDispatcher();

const initTemplate: ParseableControlObject[] = [{"type":"label","color":"#06B6D4","position":{"x":846,"y":15},"label":"Click Control"},{"type":"label","color":"#EAB308","position":{"x":350,"y":15},"label":"Hold Control"},{"type":"label","color":"#71717A","position":{"x":202,"y":72},"label":"Starts effect when held. Stops when released."},{"type":"label","color":"#71717A","position":{"x":743,"y":70},"label":"Runs effect every time it's clicked."},{"type":"label","color":"#C026D3","position":{"x":1336,"y":10},"label":"Toggle Control"},{"type":"label","color":"#71717A","position":{"x":1225,"y":70},"label":"Click to start effect. Click again to stop."},{"type":"button-hold","color":"#EAB308","position":{"x":3,"y":1},"channelID":"1","effectID":"1"},{"type":"button-tap","color":"#06B6D4","position":{"x":7,"y":1},"channelID":"1","effectID":"2"},{"type":"button-toggle","color":"#C026D3","position":{"x":11,"y":1},"channelID":"1","effectID":"3"}];
const controls: Control[] = [];

initTemplate.forEach((entry: ParseableControlObject) => {
    controls.push(Control.parseControl(entry));
});

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
    <React.StrictMode>
        <div className='absolute inset-0 h-screen w-screen bg-template-gray'>
            <App template={controls} />
        </div>
    </React.StrictMode>
)
