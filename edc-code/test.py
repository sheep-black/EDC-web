import pickle

import joblib

predictor = joblib.load('event_7.pkl')
print(predictor)
print(pickle.HIGHEST_PROTOCOL)