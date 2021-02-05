// Register Capacitor Plugin...
import '@capacitor-community/safe-area';

import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import reportWebVitals from './reportWebVitals';
import "./index.css";
import SafeAreaInsetsProvider from './safe-area-context';

ReactDOM.render(
	<React.StrictMode>
		<SafeAreaInsetsProvider>
			<App />
		</SafeAreaInsetsProvider>
	</React.StrictMode>,
	document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
