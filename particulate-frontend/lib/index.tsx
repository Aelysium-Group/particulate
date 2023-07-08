import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import { App } from './App'
import { MessageDispatcher } from './websocket-provider/providers/MessageDispatcher'

MessageDispatcher();

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
    <React.StrictMode>
        <div className='absolute inset-0 h-screen w-screen bg-template-gray'>
            <App />
        </div>
    </React.StrictMode>
)
