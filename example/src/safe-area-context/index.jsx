import * as React from 'react';
import { SafeAreaController } from '@capacitor-community/safe-area';

const StateContext = React.createContext();

export const useSafeAreaInsetsState = () => {
	const context = React.useContext(StateContext);

	if(context === undefined)
		throw new Error("Cannot use 'useSafeAreaInsetsState' outside of a SafeAreaInsetsProvider!");
	
	return context;
}

const SafeAreaInsetsProvider = ({children}) => {
	const [state, setState] = React.useState({
		top: 0,
		bottom: 0,
		right: 0,
		left: 0
	});

	React.useState(() => {
		SafeAreaController.addListener((insets) => {
			setState(insets);
		});

		SafeAreaController.load();

		return () => {
			SafeAreaController.removeAllListeners();
			SafeAreaController.unload();
		}
	}, []);

	return (
		<StateContext.Provider value={state}>
			{children}
		</StateContext.Provider>
	)
};

export default SafeAreaInsetsProvider;