# -*- coding: utf-8 -*-

"""

@author: Gregory Elleouet
"""

from setuptools import setup, find_packages
from proxieslognact import __version__ as proxyversion


setup(
    name='sen1-proxies-lognact',
    version=proxyversion,
    license='EUPL',
    author='Gregory Elleouet',
    author_email='gregory.elleouet@gmail.com',
    description="Proxy SEN1 LogNAct",
    long_description=open('README.md').read(),
    url='https://github.com/consometers/sen1-poc-proxies',
    packages=find_packages(),
    install_requires=[
        'schedule'
    ],
    entry_points={
        "console_scripts":[
            "lognactproxy = proxieslognact.proxy:main"
        ]
    }
)
