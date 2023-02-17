import * as React from 'react';
import { useEffect, useState } from 'react';
import { LogMessage } from './log-messages/LogMessage';
import { LogConfirm } from './log-messages/LogConfirm';
import { LogError } from './log-messages/LogError';
import { LogMessages, useLog } from '../../hooks/useLog';
import { EventName } from '../../events/EventName';

export const AppLog = () => {
	const [ unloadDelay ]: [ number, Function ] = useState(3);
	const [ logs, setLogs ]: [ LogMessages, Function ] = useState({});

	const log = useLog();

	const init   = (event: any) => setLogs(event.detail);
	const uninit = () => setLogs({});

	useEffect(()=>{
		document.addEventListener(EventName.TransportLogMessages, (event: any) => init(event));
		return () => {
			document.removeEventListener(EventName.TransportLogMessages,uninit);
		}
	},[]);

	return (
			<div className={`fixed right-[17px] bottom-[105px] w-80 inline-block z-50`} >
                {
					Object.entries(logs).map((entry) => {
						if(entry[1].type == 'message') return (
							<LogMessage
								key={entry[0]}
                        	    unloadDelay={unloadDelay}
                        	    index={entry[0]}
                        	   >{entry[1].contents}</LogMessage>
						);
						if(entry[1].type == 'confirm') return (
							<LogConfirm
								key={entry[0]}
                        	    unloadDelay={unloadDelay}
                        	    index={entry[0]}
                        	   >{entry[1].contents}</LogConfirm>
						);
						if(entry[1].type == 'error') return (
							<LogError
								key={entry[0]}
                        	    unloadDelay={unloadDelay}
                        	    index={entry[0]}
                        	   >{entry[1].contents}</LogError>
						);

						return (
							<LogMessage
								key={entry[0]}
                        	    unloadDelay={unloadDelay}
                        	    index={entry[0]}
                        	   >{entry[1].contents}</LogMessage>
						);
					}
				)}
			</div>
        );
	}