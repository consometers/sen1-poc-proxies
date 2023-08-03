# -*- coding: utf-8 -*-

"""

@author: Gregory Elleouet
"""

from setuptools import setup, find_packages


setup(
    name = 'sen1-proxies-lognact',
    version = '0.0.1',
    license = 'EUPL',
    author = 'Gregory Elleouet',
    author_email = 'gregory.elleouet@gmail.com',
    description = "Proxy SEN1 LogNAct",
    long_description = open('README.md').read(),
    url = 'https://github.com/consometers/sen1-poc-proxies',
    packages = find_packages(),
    install_requires = [
        'schedule',
        'requests',
        'pyzabbix',
        'sqlalchemy',
        'psycopg2-binary',
        # bug avec les certificats let's encrypt
        # https://stackoverflow.com/questions/46230357/sleekxmpp-certificate-has-expired
        'sleekxmpp==1.3.2',
        'dnspython',
        # dernière version de pyasn ne fonctionne pas avec sleekxmpp
        # https://github.com/fritzy/SleekXMPP/issues/477
        'pyasn1==0.3.7', 
        'pyasn1_modules==0.1.5',
        'protobix',
        'backports-datetime-fromisoformat'
    ],
    entry_points = {
        "console_scripts": [
            "lognactproxy = proxieslognact.proxy:main"
        ]
    }
)
